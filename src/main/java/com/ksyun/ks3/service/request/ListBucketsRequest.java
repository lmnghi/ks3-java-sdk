package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月14日 下午6:26:58
 * 
 * @description 列出当前账户下的所有bucket信息
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
