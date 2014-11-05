package com.ksyun.ks3.service.response;

import org.apache.http.HttpResponse;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月17日 下午2:40:32
 * 
 * @description 
 **/
public interface Ks3WebServiceResponse<T>{
	public T handleResponse(HttpResponse response);
	public HttpResponse getResponse();
	/**
	 * 
	 * @return 期望的 http status
	 */
	public int[] expectedStatus();
}
