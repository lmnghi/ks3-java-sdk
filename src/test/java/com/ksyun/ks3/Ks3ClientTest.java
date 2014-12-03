package com.ksyun.ks3;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ksyun.ks3.dto.*;

import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.AccessControlPolicy;
import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.CompleteMultipartUploadResult;
import com.ksyun.ks3.dto.CreateBucketConfiguration.REGION;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.Grantee;
import com.ksyun.ks3.dto.GranteeEmail;
import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.Ks3Object;
import com.ksyun.ks3.dto.Ks3ObjectSummary;
import com.ksyun.ks3.dto.ListMultipartUploadsResult;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.MultiPartUploadInfo;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.http.Ks3CoreController;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.ListObjectsRequest;
import com.ksyun.ks3.service.request.ListPartsRequest;
import com.ksyun.ks3.service.request.PutBucketACLRequest;
import com.ksyun.ks3.service.request.PutBucketLoggingRequest;
import com.ksyun.ks3.service.request.PutObjectACLRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.service.response.CompleteMultipartUploadResponse;
import com.ksyun.ks3.service.response.HeadObjectResponse;
import com.ksyun.ks3.utils.Timer;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月15日 上午10:28:53
 * 
 * @description
 **/
public class Ks3ClientTest {
	private Ks3Client client1 = new Ks3Client("GENJ6O5PQFVE37MEEMZA",
			"9Z6VbeYUJ0BiKcuwYe5x/j76TZvYe9VRh2OdH15m");
	private Ks3Client client = new Ks3Client("2HITWMQXL2VBB3XMAEHQ",
			"ilZQ9p/NHAK1dOYA/dTKKeIqT/t67rO6V2PrXUNr");
	/**
	 * 测试环境
	 */
	private Ks3Client client2 = new Ks3Client("8oN7siZgTOSGuaC1i/er",
			"ZoWO9uGe4p59aHDljcWRRecoCW+noJK11Dilz2r+");

	public static void main(String[] args) {
		String s = "<ListBucketResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Name>aw2</Name><Prefix>../../../../../../../../../../../../../../../../etc/</Prefix><Marker></Marker><MaxKeys>30</MaxKeys><Delimiter>/</Delimiter><IsTruncated>false</IsTruncated><Contents><Key>../../../../../../../../../../../../../../../../etc/passwd</Key><LastModified>2014-08-28T12:45:55.000Z</LastModified><ETag>037eef67eb8af9d2948f0e62fe78cc52</ETag><Size>17</Size><Owner><ID>46230816</ID><DisplayName>46230816</DisplayName></Owner><StorageClass>STANDARD</StorageClass></Contents><Contents><Key>../../../../../../../../../../../../../../../../etc/passwd.phpinfo.php</Key><LastModified>2014-08-28T12:46:02.000Z</LastModified><ETag>037eef67eb8af9d2948f0e62fe78cc52</ETag><Size>17</Size><Owner><ID>46230816</ID><DisplayName>46230816</DisplayName></Owner><StorageClass>STANDARD</StorageClass></Contents></ListBucketResult>";

		for (int i = 600; i < 700; i++) {
			System.out.print(s.charAt(i));
		}
	}

	@Before
	public void init() {
		ClientConfig config = ClientConfig.getConfig();
	}

	// @Test
	public void ListBuckets() {
		List<Bucket> buckets = client1.listBuckets();
		System.out.println(buckets);
	}

	// @Test
	public void getBucketLocation() {
		System.out.println(client.getBucketLoaction("ksc-scm"));
	}

	// @Test
	public void testGenerateUrl() {
		System.out.println(client.generatePresignedUrl("lijunwei.test",
				"IMG_16721.jpg", 60));
	}

	// @Test
	public void getBucketLogging() {
		System.out.println(client.getBucketLogging(""));
	}

	// @Test
	public void putBucketLogging() {
		BucketLoggingStatus status = new BucketLoggingStatus();
		status.setEnable(true);
		status.setTargetBucket("lijunwei.test");
		status.setTargetPrefix("ddd");

		GranteeEmail grantee1 = new GranteeEmail("lijunwei@kingsoft.com");
		status.addGrant(new Grant(grantee1, Permission.Read));

		GranteeUri grantee2 = GranteeUri.AllUsers;
		status.addGrant(new Grant(grantee2, Permission.FullControl));

		GranteeId grantee = new GranteeId("dwed");
		status.addGrant(new Grant(grantee, Permission.Write));

		PutBucketLoggingRequest request = new PutBucketLoggingRequest("ksc-scm");
		request.setBucketLoggingStatus(status);
		client.putBucketLogging(request);
	}

	// @Test
	public void listBucketParts() throws Exception {

		for (int i = 0; i < 100; i++) {
			ListMultipartUploadsResult result = client.listMultipartUploads(
					"ksc-scm", "我的D盘压缩.rar");
			for (MultiPartUploadInfo info : result.getUploads()) {
				if (info.getInitiated() == null)
					throw new Exception(i + "");
				// client.abortMultipartUpload("ksc-scm",info.getKey(),
				// info.getUploadId());
			}
		}
	}

	// @Test
	public void ListObjects() {

		/*
		 * ObjectListing o = client.listObjects("yyy"); Object od = o;
		 * System.out.println(od);
		 */
		ListObjectsRequest request = new ListObjectsRequest("ksc-scm", null,
				null, null, null);
		ObjectListing o = client.listObjects(request);
		Object od = o;
		System.out.println(od);
	}

	// @Test
	public void createAndDeleteBucket() {
		CreateBucketRequest request = new CreateBucketRequest("lijunwei",
				REGION.BEIJING);
		client.createBucket(request);
		client.deleteBucket("lijunwei");
	}

	// @Test
	public void getObject() throws IOException {
		GetObjectResult obj = client.getObject("ksc-scm", "123.txt");
		System.out.println(obj);
		try {
			OutputStream os = new FileOutputStream(new File("D://"
					+ obj.getObject().getKey()));
			int bytesRead = 0;
			byte[] buffer = new byte[1024 * 10];
			InputStream in = obj.getObject().getObjectContent();
			while ((bytesRead = in.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			obj.getObject().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Test
	public void deleteObject() {
		client.deleteObject("ksc-scm", "vre");
	}

	// @Test
	public void clearBucket() {
		this.client.makeDir("ksc-scm", "cewf/fewgfew/vewrgfvw/cvew/");
		this.client.makeDir("ksc-scm", "cewf/fewgfess/");
		this.client.clearBucket("ksc-scm");
	}

	// @Test
	public void removeDir() {
		this.client1.removeDir("alert1", "tt/");
	}

	// @Test
	public void makeDir() {

	}

	//@Test
	public void putObject() {
		if (!client.bucketExists("beijing.bucket")) {
			CreateBucketRequest request1 = new CreateBucketRequest(
					"beijing.bucket");
			request1.setConfig(new CreateBucketConfiguration(REGION.BEIJING));
			client.createBucket(request1);
		}
		PutObjectRequest request = new PutObjectRequest("beijing.bucket",
				"新建文件夹 (2).rar", new File("D://绯闻闻.txt"));
		client.putObject(request);

	}

	// @Test
	public void headObject() {
		HeadObjectResult response = client.headObject("lijunwei.test",
				"IMG_16721.jpg");
		System.out.println("");
	}

	// ////@Test
	public void initMultipart() {
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
				"lijunwei.test", "eclipse.zip");
		request.setCannedAcl(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client
				.initiateMultipartUpload(request);
		System.out.println(result);

	}

	// @Test
	public void uploadPart() {
		long part = 10 * 1024 * 1024;
		String bucket = "ksc-scm";
		String key = "我的D盘压缩.rar";
		// String filename = "D://新建文件夹.rar";
		String filename = "D://新建文件夹.rar";

		InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
				bucket, key);
		request1.setCannedAcl(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client
				.initiateMultipartUpload(request1);
		System.out.println(result);
		// upload
		File file = new File(filename);
		long n = file.length() / part;
		System.out.println(n);
		for (int i = 0; i <= n; i++) {
			UploadPartRequest request = new UploadPartRequest(
					result.getBucket(), result.getKey(), result.getUploadId(),
					i + 1, file, part, (long) i * part);
			PartETag tag = client.uploadPart(request);
			System.out.println(String.valueOf(i + 1) + "  " + tag + "\n");
			try {
				UploadPartTime.print(i + 1, Timer.end());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// list parts
		ListPartsRequest requestList = new ListPartsRequest(result.getBucket(),
				result.getKey(), result.getUploadId());
		ListPartsResult tags = client.listParts(requestList);
		// complete
		CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
				tags);
		client.completeMultipartUpload(request);
	}

	//@Test
	public void uploadPart_01() {
		long part = 5 * 1024 * 1024;
		String bucket = "ksc-scm";
		String key = "我的D盘压缩.rar";
		// String filename = "D://新建文件夹.rar";
		String filename = "D://新建文件夹.rar";

		InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
				bucket, key);
		request1.setCannedAcl(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client
				.initiateMultipartUpload(request1);
		System.out.println(result);
		// upload
		File file = new File(filename);
		long n = file.length() / part + 1000000;
		System.out.println(n);
		for (int i = 1000000; i <= n; i++) {
			UploadPartRequest request = new UploadPartRequest(
					result.getBucket(), result.getKey(), result.getUploadId(),
					i + 1, file, part, (long) i * part);
			PartETag tag = client.uploadPart(request);
			System.out.println(String.valueOf(i + 1) + "  " + tag + "\n");
			try {
				UploadPartTime.print(i + 1, Timer.end());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// list parts
		ListPartsRequest requestList = new ListPartsRequest(result.getBucket(),
				result.getKey(), result.getUploadId());
		ListPartsResult tags = client.listParts(requestList);
		// complete
		CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
				tags);
		client.completeMultipartUpload(request);
	}

	// @Test
	public void testETag() {
		List<String> s = new ArrayList<String>();
		for (int m = 0; m < 100; m++) {
			long part = 5 * 1024 * 1024;
			String bucket = "ksc-scm";
			String key = m + ".jpeg";
			// String filename = "D://新建文件夹.rar";
			String filename = "D://1234.jpeg";

			InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
					bucket, key);
			request1.setCannedAcl(CannedAccessControlList.PublicRead);
			InitiateMultipartUploadResult result = client
					.initiateMultipartUpload(request1);
			System.out.println(result);
			// upload
			File file = new File(filename);
			long n = file.length() / part;
			System.out.println(n);
			for (int i = 0; i <= n; i++) {
				UploadPartRequest request = new UploadPartRequest(
						result.getBucket(), result.getKey(),
						result.getUploadId(), i + 1, file, part, (long) i
								* part);
				PartETag tag = client.uploadPart(request);
			}
			// list parts
			ListPartsRequest requestList = new ListPartsRequest(
					result.getBucket(), result.getKey(), result.getUploadId());
			ListPartsResult tags = client.listParts(requestList);
			// complete
			CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
					tags);
			client.completeMultipartUpload(request);
		}
		System.out.println(s);
	}

	// @Test
	public void completeMulti() {
		CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
				"lijunwei.test", "eclipse.zip",
				"aedf953924f34d0ba93b74188de1596b", null);
		request.addETag(new PartETag(1, "d41d8cd98f00b204e9800998ecf8427e"));
		request.addETag(new PartETag(2, "d41d8cd98f00b204e9800998ecf8427e"));
		request.addETag(new PartETag(3, "d41d8cd98f00b204e9800998ecf8427e"));
		CompleteMultipartUploadResult response = client
				.completeMultipartUpload(request);
		System.out.println(response);
	}

	// @Test
	public void listParts() {
		ListPartsResult result = client.listParts("ksc-scm", "我的D盘压缩.rar",
				"ec7f258585e04cf1998503d3db7c3826");
		System.out.println(result);
	}

	// @Test
	public void abortMulti() {
		client.abortMultipartUpload("", "bigFile.rar",
				"44157d71c6e741699c8c5fb1f4f61aff");
	}

	// //@Test
	public void getBucketACL() {
		AccessControlPolicy getBucketACL = client.getBucketACL("ksc-scm");
		System.out.println(getBucketACL.getAccessControlList());
	}

	// @Test
	public void putBucketACL() {
		/*
		 * if (client2.bucketExists("lijunwei.test")) {
		 * client2.clearBucket("lijunwei.test");
		 * client2.deleteBucket("lijunwei.test");
		 * client2.createBucket("lijunwei.test"); }
		 */
		PutBucketACLRequest request = new PutBucketACLRequest("lijunwei.test");

		AccessControlList acl = new AccessControlList();

		Grant grant1 = new Grant();
		GranteeId grantee1 = new GranteeId("1233ddd");
		grant1.setGrantee(grantee1);
		grant1.setPermission(Permission.Write);

		Grant grant2 = new Grant();
		GranteeId grantee2 = new GranteeId("ddddx");
		grant2.setGrantee(grantee2);
		grant2.setPermission(Permission.Write);

		Grant grant3 = new Grant();
		GranteeId grantee3 = new GranteeId("dwdqwdqw");
		grant3.setGrantee(grantee3);
		grant3.setPermission(Permission.Write);

		acl.addGrant(grant1);
		acl.addGrant(grant2);
		acl.addGrant(grant3);
		// request.setAccessControlList(acl);
		request.setCannedAcl(CannedAccessControlList.PublicRead);
		client2.putBucketACL(request);
		System.out.println(client2.getBucketACL("lijunwei.test"));
	}

	// //@Test
	public void putObjectACL() {
		PutObjectACLRequest request = new PutObjectACLRequest("ksc-scm",
				"这个事测试.doc");
		AccessControlList acl = new AccessControlList();
		/*
		 * Grantee grantee = new GranteeId();
		 * grantee.setIdentifier("1E74015858B022A60108039F");
		 */
		acl.addGrant(GranteeUri.AllUsers, Permission.Read);
		request.setAccessControlList(acl);
		// request.setCannedAcl(CannedAccessControlList.Private);
		client.putObjectACL(request);
	}

	// //@Test
	public void getObjectACL() {
		AccessControlPolicy getObjectACL = client.getObjectACL("ksc-scm",
				"这个事测试.doc");
		System.out.println(getObjectACL.getAccessControlList());
	}

	// ////@Test
	public void configBucketAcl() {
		PutBucketACLRequest request = new PutBucketACLRequest("ksc-scm",
				CannedAccessControlList.PublicReadWrite);
		client.putBucketACL(request);
	}

	// @Test
	public void deleteObjects() {
		System.out.println(client.deleteObjects(new String[] {
				"11112018rln5.pdf", "dfdfdsf.pdf", "sssss", "square/",
				"prop.txt" }, "ksc-scm"));
	}

	static int i = 0;

	// @Test
	public void test() {

		for (;; i++) {
			partDownLoad();
		}
	}

	// @Test
	public void partDownLoad() {
		GetObjectRequest request = new GetObjectRequest("lijunwei.test",
				"IMG_16721。exe");
		long max = 1024 * 1024;
		long index = 0;
		long step = 1024 * 1024 * 1024;
		for (; index <= max; index = index + step + 1) {
			request.setRange(index, index + step);
			GetObjectResult result = client.getObject(request);
			max = result.getObject().getObjectMetadata().getInstanceLength();

			try {
				OutputStream os = new FileOutputStream(new File("D://ggg/" + i
						+ "--" + result.getObject().getKey()), true);

				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = result.getObject().getObjectContent()
						.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				result.getObject().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// @Test
	public void putFileByStream() throws Ks3ServiceException,
			Ks3ClientException, FileNotFoundException {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType("txt");
		// meta.setContentLength(1);
		client.PutObject("ksc-scm", "bystream.et", new FileInputStream(
				new File("D://work//API//新建文本文档.txt")), meta);
		System.out.println(client.getObject("ksc-scm", "bystream.et"));
	}

	// @Test
	public void uploadPartByStream() throws FileNotFoundException {
		InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
				"ksc-scm", "bystream.txt");
		request1.setCannedAcl(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client
				.initiateMultipartUpload(request1);
		System.out.println(result);
		UploadPartRequest request = new UploadPartRequest(result.getBucket(),
				result.getKey(), result.getUploadId(), 1, new FileInputStream(
						new File("D://work//API//新建文本文档.txt")), 1);
		// request.setContentMd5("122");
		client.uploadPart(request);
		UploadPartRequest request3 = new UploadPartRequest(result.getBucket(),
				result.getKey(), result.getUploadId(), 2, new InputSubStream(
						new FileInputStream(new File(
								"D://work//API//新建文本文档.txt")), 1L, 1L, true), 1);
		// request.setContentMd5("122");
		client.uploadPart(request3);
		// list parts
		ListPartsRequest requestList = new ListPartsRequest(result.getBucket(),
				result.getKey(), result.getUploadId());
		ListPartsResult tags = client.listParts(requestList);
		System.out.println(tags);
		// complete
		CompleteMultipartUploadRequest request2 = new CompleteMultipartUploadRequest(
				tags);
		client.completeMultipartUpload(request2);
	}
}
