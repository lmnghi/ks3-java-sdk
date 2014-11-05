package com.ksyun.ks3.service.request;

import org.apache.commons.lang.StringUtils;

import com.ksyun.ks3.http.HttpMethod;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 下午7:40:04
 * 
 * @description 
 **/
public class HeadObjectRequest extends Ks3WebServiceRequest {
	public HeadObjectRequest(String bucketname,String objectkey)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.HEAD);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
	}

}
