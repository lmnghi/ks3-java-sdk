package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月21日 下午2:15:06
 * 
 * @description 
 **/
public class HeadBucketRequest extends Ks3WebServiceRequest{
	public HeadBucketRequest(String bucketname)
	{
		super.setBucketname(bucketname);
	}
	public void setBucketname(String bucketname)
	{
		super.setBucketname(bucketname);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.HEAD);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
	}

}
