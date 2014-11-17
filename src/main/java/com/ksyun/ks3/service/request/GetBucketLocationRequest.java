package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月16日 下午6:45:23
 * 
 * @description 获取 bucket的存储地点
 **/
public class GetBucketLocationRequest extends Ks3WebServiceRequest{
	public GetBucketLocationRequest(String bucketName){
		super.setBucketname(bucketName);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("location",null);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
	}

}
