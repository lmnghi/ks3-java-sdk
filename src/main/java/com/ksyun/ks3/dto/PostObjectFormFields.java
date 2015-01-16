package com.ksyun.ks3.dto;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年1月14日 下午2:58:45
 * 
 * @description post object时需要从后台提供的内容
 **/
public class PostObjectFormFields {
	private String policy;
	private String kssAccessKeyId;
	private String signature;
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	public String getKssAccessKeyId() {
		return kssAccessKeyId;
	}
	public void setKssAccessKeyId(String kssAccessKeyId) {
		this.kssAccessKeyId = kssAccessKeyId;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
