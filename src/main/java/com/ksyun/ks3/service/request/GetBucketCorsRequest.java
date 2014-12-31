package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;


/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月31日 下午1:46:05
 * 
 * @description 获取bucket的跨域资源共享配置
 **/
public class GetBucketCorsRequest extends Ks3WebServiceRequest{

	public GetBucketCorsRequest(String bucketName){
		super.setBucketname(bucketName);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("cors","");
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(super.getBucketname()))
			throw notNull("bucketName");
	}

}
