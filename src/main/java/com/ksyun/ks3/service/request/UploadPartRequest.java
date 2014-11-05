package com.ksyun.ks3.service.request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.ksyun.ks3.InputSubStream;
import com.ksyun.ks3.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.RepeatableFileInputStream;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 上午11:17:36
 * 
 * @description 
 **/
public class UploadPartRequest extends Ks3WebServiceRequest{	
	private static final int minPartNumber = 1;
	private static final int maxPartNumber = 10000;
	private static final int minPartSize = 5*1024*1024;
	private static final int maxPartSize = 5*1024*1024*1024;
	
	private String uploadId;
	private int partNumber;
	private File file;
	private long partSize;
	private long fileoffset;
	public UploadPartRequest(String bucketname,String objectkey,String uploadId,int partNumber,File file,long partsize,long fileoffset)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.setUploadId(uploadId);
		this.setPartNumber(partNumber);
		this.setFile(file);
		this.setPartSize(partsize);
		this.setFileoffset(fileoffset);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("uploadId", this.uploadId);
		this.addParams("partNumber",String.valueOf(this.partNumber));
		try {
			InputStream inputStream = new MD5DigestCalculatingInputStream(new InputSubStream(new RepeatableFileInputStream(this.file),
			        this.fileoffset, partSize, true));
			this.setRequestBody(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Ks3ClientException("read file "+file.getName()+" error");
		}
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
}
