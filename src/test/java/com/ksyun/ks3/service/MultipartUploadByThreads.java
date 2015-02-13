package com.ksyun.ks3.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.utils.Md5Utils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年1月27日 下午1:53:08
 * 
 * @description
 **/
public class MultipartUploadByThreads extends Ks3ClientTest {
	long part = 5 * 1024 * 1024;
	String bucketName = "afiles";
	File file = null;
	String filename = "file";
	String dir = "D://multithread/";
	int maxThreads = 20;
	int extThread = 1;

	@Before
	public void initFile() throws IOException {
		file = new File(dir + filename);
		file.delete();
		file = new File(dir + filename);
		new File(dir).mkdir();
		FileWriter writer = new FileWriter(file);
		for (int j = 0; j < 10; j++) {
			StringBuilder value = new StringBuilder();
			String sub = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
			for (int i = 0; i < 300000; i++) {
				value.append(sub);
			}
			writer.append(value.toString());
		}
		writer.close();
		if (!client1.bucketExists(bucketName)) {
			client1.createBucket(bucketName);
		}
	}

	@After
	public void after() {
/*		if (client1.bucketExists(bucketName)) {
			client1.clearBucket(bucketName);
			client1.deleteBucket(bucketName);
		}*/
	}

	@Test
	public void uploadAndDownloadByMutiThreads() throws FileNotFoundException,
			IOException, Exception {
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
				bucketName, filename);
		final InitiateMultipartUploadResult initResult = client1
				.initiateMultipartUpload(request);
		final List<PartETag> partEtags = new ArrayList<PartETag>();

		int partnums = (int) (1 + (file.length() / part));

		ExecutorService pool = Executors.newFixedThreadPool(maxThreads*(1+extThread));

		// 上传块的线程
		for (int i = 0; i < partnums; i++) {
			final int partNum = i;
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						UploadPartRequest upRequest = new UploadPartRequest(
								bucketName, filename, initResult.getUploadId(),
								partNum + 1, file, part, part * partNum);
						PartETag upResult = client1.uploadPart(upRequest);
						partEtags.add(upResult);
					} catch (Ks3ClientException e) {
						run();
					}
				}
			};
			for (int j = 0; j < extThread; j++) {
				// 额外的线程是为了测试覆盖
				Thread t1 = new Thread() {
					@Override
					public void run() {
						try {
							UploadPartRequest upRequest = new UploadPartRequest(
									bucketName, filename,
									initResult.getUploadId(), partNum + 1,
									file, part, part * partNum);
							client1.uploadPart(upRequest);
						} catch (Ks3ClientException e) {
							run();
						}
					}
				};
				pool.execute(t1);
			}
			pool.execute(t);

		}

		pool.shutdown();
		for (;;) {
			if (pool.isTerminated())
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		client1.completeMultipartUpload(bucketName, filename,
				initResult.getUploadId(), partEtags);

		HeadObjectResult result = client1.headObject(bucketName, filename);
		final long contentLength = result.getObjectMetadata()
				.getContentLength();
		int parts = (int) (contentLength / part) + 1;
		// 多线程下载
		ExecutorService poolDown = Executors.newFixedThreadPool(maxThreads);
		for (int i = 0; i < parts; i++) {
			final int partNum = i;
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						long start = partNum * part;
						long end = (partNum + 1) * part - 1;
						if (end > contentLength) {
							end = contentLength;
						}
						new File(dir + filename + "-part" + partNum).delete();
						GetObjectRequest req = new GetObjectRequest(bucketName,
								filename);
						req.setRange(start, end);
						GetObjectResult result = client1.getObject(req);
						InputStream content = result.getObject()
								.getObjectContent();
						OutputStream os = new FileOutputStream(new File(dir
								+ filename + "-part" + partNum));

						int bytesRead = 0;
						byte[] buffer = new byte[8192];
						try {
							while ((bytesRead = content.read(buffer, 0, 8192)) != -1) {
								os.write(buffer, 0, bytesRead);
							}
						} finally {
							try {
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							try {
								content.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (Throwable e) {
						System.out.println(Thread.currentThread().getName());
						e.printStackTrace();
						run();
					}
				}
			};
			poolDown.execute(t);
		}
		poolDown.shutdown();
		for (;;) {
			if (poolDown.isTerminated())
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		List<String> partFiles = new ArrayList<String>();
		for (int i = 0; i < parts; i++) {
			partFiles.add(dir + filename + "-part" + i);
		}
		mergeFiles(dir + filename + "-Down", partFiles.toArray());
		assertEquals(Md5Utils.md5AsBase64(file),
				Md5Utils.md5AsBase64(new File(dir + filename + "-Down")));
		System.out.println(initResult);
	}

	private static final int BUFSIZE = 1024 * 8;

	public static void mergeFiles(String outFile, Object[] files) {
		new File(outFile).delete();
		FileChannel outChannel = null;
		try {
			outChannel = new FileOutputStream(outFile).getChannel();
			for (Object f : files) {
				FileChannel fc = new FileInputStream(f.toString()).getChannel();
				ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
				while (fc.read(bb) != -1) {
					bb.flip();
					outChannel.write(bb);
					bb.clear();
				}
				fc.close();
				new File(f.toString()).delete();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException ignore) {
			}
		}
	}
}
