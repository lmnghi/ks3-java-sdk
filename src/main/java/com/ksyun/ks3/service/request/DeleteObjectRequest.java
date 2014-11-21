package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:45:23
 * 
 * @description 删除单个object的请求
 **/
public class DeleteObjectRequest extends Ks3WebServiceRequest{
	public DeleteObjectRequest(String bucketname,String key)
	{
		setBucketname(bucketname);
		setObjectkey(key);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.DELETE);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
	}

}
