package com.ksyun.ks3.request;

import java.io.ByteArrayInputStream;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.CreateBucketRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月11日 下午1:39:41
 * 
 * @description 
 **/
public class ErrorRegionCreateBucketRequest extends CreateBucketRequest{

	public ErrorRegionCreateBucketRequest(String bucketName) {
		super(bucketName);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.setRequestBody(new ByteArrayInputStream("<CreateBucketConfiguration xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"> <LocationConstraint>TAIYUAN</LocationConstraint> </CreateBucketConfiguration>".getBytes()));
	}

}
