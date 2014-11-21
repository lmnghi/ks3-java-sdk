package com.ksyun.ks3.service.request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ksyun.ks3.InputSubStream;
import com.ksyun.ks3.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.RepeatableFileInputStream;
import com.ksyun.ks3.RepeatableInputStream;
import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.support.MD5CalculateAble;
import com.ksyun.ks3.utils.Md5Utils;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 上午11:17:36
 * 
 * @description 分块上传时，Upload Part的请求
 *              <p>
 *              支持提供文件对象进行分块上传，请使用UploadPartRequest(String bucketname, String
 *              objectkey, String uploadId, int partNumber, File file, long
 *              partsize, long fileoffset)
 *              </p>
 *              <p>
 *              支持提供已经切分好的流进行分块上传，请提供流的长度，请使用 UploadPartRequest(String
 *              bucketname, String objectkey, String uploadId, int partNumber,
 *              InputStream content, long partSize, String contentMd5)
 *              </p>
 **/
public class UploadPartRequest extends Ks3WebServiceRequest implements
		MD5CalculateAble {
	private final Log log = LogFactory.getLog(UploadPartRequest.class);

	/**
	 * 由init multipart upload获取到的uploadId
	 */
	private String uploadId;
	/**
	 * 分块上传时对块的编号，1-10000，请保证partNumber是连续的
	 */
	private int partNumber;
	/**
	 * 要上传的文件
	 */
	private File file;
	/**
	 * 当前块的大小，文件的时候最后一块大小不一定要准确，但是流的时候需要准确
	 */
	private long partSize;
	/**
	 * 文件的时候，之前已经读取的数据量
	 */
	private long fileoffset;
	/**
	 * 使用流的时候，要上传的内容
	 */
	private InputStream content;

	/**
	 * 
	 * @param bucketname
	 * @param objectkey
	 * @param uploadId
	 * @param partNumber
	 * @param file
	 * @param partsize
	 *            注意类型为long
	 * @param fileoffset
	 *            注意类型为long
	 */
	public UploadPartRequest(String bucketname, String objectkey,
			String uploadId, int partNumber, File file, long partsize,
			long fileoffset) {
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.setUploadId(uploadId);
		this.setPartNumber(partNumber);
		this.setFile(file);
		this.setPartSize(partsize);
		this.setFileoffset(fileoffset);
		this.partSize = file.length() - fileoffset < partsize ? file
				.length() - fileoffset : partsize;
	}

	/**
	 * 
	 * @param bucketname
	 * @param objectkey
	 * @param uploadId
	 * @param partNumber
	 * @param content
	 *            要上传的块的inputstream,(已经切分好的块)
	 * @param partSize
	 *            content的长度,必须提供
	 * @param contentMd5
	 *            <p>
	 *            可以指定content-md5否则sdk将不在服务端进行MD5校验
	 *            </p>
	 */
	public UploadPartRequest(String bucketname, String objectkey,
			String uploadId, int partNumber, InputStream content,
			long partSize, String contentMd5) {
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.setUploadId(uploadId);
		this.setPartNumber(partNumber);
		this.setPartSize(partSize);
		this.setContentMD5(contentMd5);
		this.setContent(content);
	}

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("uploadId", this.uploadId);
		this.addParams("partNumber", String.valueOf(this.partNumber));
		if (this.file != null) {
			try {
				content = new InputSubStream(new RepeatableFileInputStream(
						this.file), this.fileoffset, partSize, true);

			} catch (FileNotFoundException e) {
				throw new Ks3ClientException("read file " + file.getName()
						+ " error", e);
			}
		} else {
			this.content = new RepeatableInputStream(content,
					Constants.DEFAULT_STREAM_BUFFER_SIZE);
		}
		this.addHeader(HttpHeaders.ContentLength,
				String.valueOf(this.partSize));
		this.setRequestBody(content);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if (StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if (StringUtils.isBlank(this.uploadId))
			throw new IllegalArgumentException("uploadId can not be null");
		if (partNumber < Constants.minPartNumber
				|| partNumber > Constants.maxPartNumber)
			throw new IllegalArgumentException("partNumber shoud between "
					+ Constants.minPartNumber + " and "
					+ Constants.maxPartNumber);
		if (file == null && content == null) {
			throw new IllegalArgumentException(
					"file and content can not both be null");
		} else {
			if (file != null) {
				if (this.fileoffset < 0)
					throw new IllegalArgumentException("fileoffset("
							+ this.fileoffset + ") should >= 0");
				if (this.partSize < Constants.minPartSize
						|| this.partSize > Constants.maxPartSize) {
					throw new IllegalArgumentException("partsize("
							+ this.partSize + ") should between "
							+ Constants.minPartSize + " and "
							+ Constants.maxPartSize);
				}
			} else {

			}
		}
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getPartSize() {
		return partSize;
	}

	public void setPartSize(long partSize) {
		this.partSize = partSize;
	}

	public long getFileoffset() {
		return fileoffset;
	}

	public void setFileoffset(long fileoffset) {
		this.fileoffset = fileoffset;
	}
	public String getMd5() {
		if (!StringUtils.isBlank(this.getContentMD5()))
			return this.getContentMD5();
		else
			return com.ksyun.ks3.utils.Base64
					.encodeAsString(((MD5DigestCalculatingInputStream) super
							.getRequestBody()).getMd5Digest());
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}
}
