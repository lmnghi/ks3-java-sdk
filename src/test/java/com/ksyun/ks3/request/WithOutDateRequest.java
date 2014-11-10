package com.ksyun.ks3.request;

import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 上午11:27:14
 * 
 * @description 
 **/
public class WithOutDateRequest extends Ks3WebServiceRequest{

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		this.getHeader().remove(HttpHeaders.Date);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		
	}

}
