package com.ksyun.ks3.request;

import java.io.ByteArrayInputStream;

import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.CreateBucketRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月11日 下午1:49:48
 * 
 * @description 
 **/
public class ErrorCannedAclCreateBucketRequest extends CreateBucketRequest{

	private String acl;
	public ErrorCannedAclCreateBucketRequest(String bucketName,String acl) {
		super(bucketName);
		this.acl = acl;
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addHeader(HttpHeaders.CannedAcl.toString(),acl);
	}
}
