package com.ksyun.ks3.service.request;

import com.google.common.net.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月20日 下午7:55:25
 * 
 * @description 
 **/
public class GetObjectRequest extends Ks3WebServiceRequest {
	private String range = null;
	public GetObjectRequest(String bucketname,String key)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(key);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		if(!StringUtils.isBlank(range))
			this.addHeader(HttpHeaders.RANGE,range);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if(!StringUtils.isBlank(range))
		{
			if(!range.startsWith("bytes="))
				throw new IllegalArgumentException("Range should be start with 'bytes='");
		}
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
}
