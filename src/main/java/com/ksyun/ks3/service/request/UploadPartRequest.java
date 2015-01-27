package com.ksyun.ks3.service.request;

import java.io.File;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNullInCondition;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notCorrect;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.between;

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
import com.ksyun.ks3.exception.client.ClientFileNotFoundException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.support.MD5CalculateAble;
import com.ksyun.ks3.utils.Md5Utils;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月23日 上午11:17:36
 * 
 * @description 分块上传时，Upload Part的请求
 *              <p>
 *              可以通过setContentMd5()指定MD5值，使其在服务端进行md5值校验，否则只会在客户端校验(区别:
 *              前者校验失败不会上传成功)
 *              </p>
 *              <p>
 *              支持提供文件对象进行分块上传，请使用UploadPartRequest(String bucketname, String
 *              objectkey, String uploadId, int partNumber, File file, long
 *              partsize, long fileoffset)
 *              </p>
 *              <p>
 *              支持提供已经切分好的流进行分块上传，请提供流的长度，请使用 UploadPartRequest(String
 *              bucketname, String objectkey, String uploadId, int partNumber,
 *              InputStream content, long partSize)
 *              </p>
 * 
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
	 * 
	 * @param bucketname
	 * @param objectkey
	 * @param uploadId
	 * @param partNumber
	 * @param file
	 * @param partsize
	 *            注意类型为long,块的大小，除最后一块外需要给准确数字。必须提供,最大为5G,除最后一块最小为5M
	 * @param fileoffset
	 *            注意类型为long，文件中已被读取的量
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
	 *            content的长度,必须提供,最大为5G,除最后一块最小为5M
	 */
	public UploadPartRequest(String bucketname, String objectkey,
			String uploadId, int partNumber, InputStream content, long partSize) {
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.setUploadId(uploadId);
		this.setPartNumber(partNumber);
		this.setPartSize(partSize);
		this.setRequestBody(content);
	}

	@Override
	protected void configHttpRequest() {
		
		this.setContentType("binary/octet-stream");
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("uploadId", this.uploadId);
		this.addParams("partNumber", String.valueOf(this.partNumber));
		if (this.file != null) {
			this.partSize = file.length() - fileoffset < partSize ? file.length()
					- fileoffset : partSize;
			try {
				this.setRequestBody(new InputSubStream(new RepeatableFileInputStream(
						this.file), this.fileoffset, partSize, true));

			} catch (FileNotFoundException e) {
				throw new ClientFileNotFoundException(e);
			}
		} else {
			this.setRequestBody(new RepeatableInputStream(this.getRequestBody(),
					Constants.DEFAULT_STREAM_BUFFER_SIZE));
		}
		this.addHeader(HttpHeaders.ContentLength, String.valueOf(this.partSize));
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.isBlank(this.getBucketname()))
			throw notNull("bucketname");
		if (StringUtils.isBlank(this.getObjectkey()))
			throw notNull("objectkey");
		if (StringUtils.isBlank(this.uploadId))
			throw notNull("uploadId");
		if (partNumber < Constants.minPartNumber
				|| partNumber > Constants.maxPartNumber)
			throw between("partNumber",String.valueOf(this.partNumber),String.valueOf(Constants.minPartNumber),String.valueOf(Constants.maxPartNumber));
		if (file == null && this.getRequestBody() == null) {
			throw notNull(
					"file(要上传的文件)","content(要上传的流)");
		} else {
			if (file != null) {
				if (this.fileoffset < 0||this.fileoffset>file.length())
					throw between("fileoffset",String.valueOf(this.fileoffset),"0",
							String.valueOf(file.length()));
			}
		}
		if (this.partSize > Constants.maxPartSize) {
			throw between("partsize",String.valueOf(this.partSize),"0",
					String.valueOf(Constants.maxPartSize));
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


	public String getContentMD5() {
		return super.getContentMD5();
	}

	public void setContentMD5(String contentMd5) {
		super.setContentMD5(contentMd5);
	}
}
