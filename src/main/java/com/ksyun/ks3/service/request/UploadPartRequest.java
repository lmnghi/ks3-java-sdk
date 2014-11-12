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
 * @description 
 **/
public class UploadPartRequest extends Ks3WebServiceRequest implements MD5CalculateAble{	
	private final Log log = LogFactory.getLog(UploadPartRequest.class);
	private static final int minPartNumber = 1;
	private static final int maxPartNumber = 10000;
	private static final int minPartSize = 5*1024*1024;
	private static final int maxPartSize = 5*1024*1024*1024;
	
	private String uploadId;
	private int partNumber;
	private File file;
	private long partSize;
	private long fileoffset;
	private long contentLength = -1;
	/**
	 * 将添加100-continue的header，在确定可以传输数据之后才真正上传数据
	 * @param expectContinue
	 */
	private boolean expectContinue = false;
	/**
	 * 
	 * @param bucketname
	 * @param objectkey
	 * @param uploadId
	 * @param partNumber
	 * @param file
	 * @param partsize 注意类型为long
	 * @param fileoffset 注意类型为long
	 */
	public UploadPartRequest(String bucketname,String objectkey,String uploadId,int partNumber,File file,long partsize,long fileoffset)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.setUploadId(uploadId);
		this.setPartNumber(partNumber);
		this.setFile(file);
		this.setPartSize(partsize);
		this.setFileoffset(fileoffset);
		this.contentLength = file.length()-fileoffset<partsize?file.length()-fileoffset:partsize;
	}
	@Override
	protected void configHttpRequest() {
		if(this.expectContinue)
			this.addHeader(HttpHeaders.Expect,"100-continue");
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("uploadId", this.uploadId);
		this.addParams("partNumber",String.valueOf(this.partNumber));
		InputStream inputStream = null;
		try {
			inputStream = new InputSubStream(new RepeatableFileInputStream(this.file),
			        this.fileoffset, partSize, true);

		} catch (FileNotFoundException e) {
			throw new Ks3ClientException("read file "+file.getName()+" error");
		} 
	    this.addHeader(HttpHeaders.ContentLength,String.valueOf(this.contentLength));
		this.setRequestBody(inputStream);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if(StringUtils.isBlank(this.uploadId))
			throw new IllegalArgumentException("uploadId can not be null");
		if(partNumber<minPartNumber||partNumber>maxPartNumber)
			throw new IllegalArgumentException("partNumber shoud between "+minPartNumber+" and "+maxPartNumber);
		if(file==null)
		{
			throw new IllegalArgumentException("file can not be null");
		}else
		{
			if(this.fileoffset<0)
				throw new IllegalArgumentException("fileoffset("+this.fileoffset+") should >= 0");
		    if(this.partSize<minPartSize||this.partSize>maxPartSize)
		    {
		    	throw new IllegalArgumentException("partsize("+this.partSize+") should between "+minPartSize+" and "+maxPartSize);
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
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public String getMd5() {
		return com.ksyun.ks3.utils.Base64
				.encodeAsString(((MD5DigestCalculatingInputStream)super.getRequestBody())
						.getMd5Digest());
	}
	public boolean isExpectContinue() {
		return expectContinue;
	}
	/**
	 * 将添加100-continue的header，在确定可以传输数据之后才真正上传数据
	 * @param expectContinue
	 */
	public void setExpectContinue(boolean expectContinue) {
		expectContinue = expectContinue;
	}
	
}
