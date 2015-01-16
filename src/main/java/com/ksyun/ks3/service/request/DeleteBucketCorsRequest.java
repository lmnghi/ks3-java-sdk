package com.ksyun.ks3.service.request;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月31日 下午3:36:02
 * 
 * @description 删除bucket的跨域资源共享配置
 **/
public class DeleteBucketCorsRequest extends Ks3WebServiceRequest{
	public DeleteBucketCorsRequest(String bucketName){
		super.setBucketname(bucketName);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.DELETE);
		this.addParams("cors","");
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(super.getBucketname()))
			throw notNull("bucketName");
	}

}
