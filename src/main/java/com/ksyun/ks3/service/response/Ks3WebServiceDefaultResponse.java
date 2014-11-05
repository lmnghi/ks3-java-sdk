package com.ksyun.ks3.service.response;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.ksyun.ks3.exception.Ks3ClientException;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月20日 下午5:13:22
 * 
 * @description 
 **/
public abstract class Ks3WebServiceDefaultResponse<T> implements Ks3WebServiceResponse<T>{
	protected T result = null;
	protected HttpResponse response;
	public T handleResponse(HttpResponse response) {
		this.response = response;
		preHandle();
		return result;
	}
	public abstract void preHandle();
	public HttpResponse getResponse() {
		return this.response;
	}
	protected Header[] getHeaders(String key)
	{
		return response.getHeaders(key);
	}
	protected String getHeader(String key)
	{
		Header[] headers = getHeaders(key);
		if(headers.length>0)
		{
			return headers[0].getValue();
		}
		return "";
	}
	protected InputStream getContent()
	{
		try {
			return response.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Ks3ClientException("faild to get the response content("+e+")",e);
		}
	}
}
