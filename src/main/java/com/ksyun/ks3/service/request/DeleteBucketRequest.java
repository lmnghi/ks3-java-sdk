package com.ksyun.ks3.service.request;

import org.apache.commons.lang.StringUtils;

import com.ksyun.ks3.http.HttpMethod;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:07:07
 * 
 * @description 
 **/
public class DeleteBucketRequest extends Ks3WebServiceRequest{
	public DeleteBucketRequest(String bucketname)
	{
		setBucketname(bucketname);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.DELETE);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name is not correct");
	}
}
