package com.ksyun.ks3.dto;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 下午1:43:32
 * 
 * @description 
 **/
public class CompleteMultipartUploadResult {
	/**
	 * 新建对象的uri
	 */
	private String location;
	private String bucket;
	private String key;
	private String eTag;
	public String toString()
	{
		return StringUtils.object2string(this);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String geteTag() {
		return eTag;
	}
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
}
