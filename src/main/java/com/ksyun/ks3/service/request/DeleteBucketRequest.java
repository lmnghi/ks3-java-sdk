package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月16日 下午3:07:07
 * 
 * @description 删除bucket的请求
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
