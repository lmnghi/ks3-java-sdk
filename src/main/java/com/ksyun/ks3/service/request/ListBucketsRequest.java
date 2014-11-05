package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午6:26:58
 * 
 * @description 
 **/
public class ListBucketsRequest extends Ks3WebServiceRequest{

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		
	}
}
