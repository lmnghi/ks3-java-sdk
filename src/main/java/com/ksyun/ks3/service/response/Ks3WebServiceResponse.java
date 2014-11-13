package com.ksyun.ks3.service.response;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月17日 下午2:40:32
 * 
 * @description 
 **/
public interface Ks3WebServiceResponse<T>{
	public static int allStatueCode = -1;
	public T handleResponse(HttpRequest httpRequest, HttpResponse response);
	public HttpResponse getResponse();
	public String getRequestId();
	/**
	 * 
	 * @return 期望的 http status
	 */
	public int[] expectedStatus();
}
