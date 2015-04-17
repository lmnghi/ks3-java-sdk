package com.ksyun.ks3.http;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月17日 下午5:30:22
 * 
 * @description 
 **/
public class Request {
	private String endpoint;
	private String resourcePath;
	private Map<String,String> queryParams = new HashMap<String,String>();
	private Map<String,String> headers = new HashMap<String,String>();
	private InputStream content;
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public Map<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public void addHeader(String key,String value){
		this.headers.put(key, value);
	}
	public void addHeaderIfNotContains(String key,String value){
		if(!this.headers.containsKey(key))
			this.addHeader(key, value);
	}
	public InputStream getContent() {
		return content;
	}
	public void setContent(InputStream content) {
		this.content = content;
	}
	
}
