package com.ksyun.ks3.dto;


/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月15日 下午1:43:03
 * 
 * @description 
 **/
public class Authorization {
	private String accessKeyId;
	private String accessKeySecret;
	public Authorization(String id,String secret)
	{
		this.accessKeyId = id;
		this.accessKeySecret = secret;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
}
