package com.ksyun.ks3.dto;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import com.ksyun.ks3.AutoAbortInputStream;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:54:01
 * 
 * @description 存放object信息
 **/
public class Ks3Object  implements Closeable{
	
	private String key = null;
	private String bucketName = null;
	/**
	 * object 元数据
	 */
	private ObjectMetadata objectMetadata;
	/**
	 * object 数据
	 */
	private AutoAbortInputStream objectContent;
	private String redirectLocation;
	
	@Override
	public String toString()
	{
		return "Ks3Object[bucket="+this.bucketName+";key="+this.key+";redirectLocation="+this.redirectLocation+";objectMetadata="+this.objectMetadata+"]";
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public AutoAbortInputStream getObjectContent() {
		return objectContent;
	}

	public void setObjectContent(AutoAbortInputStream objectContent) {
		this.objectContent = objectContent;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}

	public void close() throws IOException {
		if(objectContent!=null)
		{
			objectContent.close();
		}
	}

	public ObjectMetadata getObjectMetadata() {
		return objectMetadata;
	}

	public void setObjectMetadata(ObjectMetadata objectMetadata) {
		this.objectMetadata = objectMetadata;
	}

}
