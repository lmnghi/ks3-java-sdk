package com.ksyun.ks3.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午12:00:19
 * 
 * @description 
 **/
public class HeadBucketResult {
	private int statueCode;
	private Map<String,String> headers = new HashMap<String,String>();
	public int getStatueCode() {
		return statueCode;
	}
	public void setStatueCode(int statueCode) {
		this.statueCode = statueCode;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String toString()
	{
		return "HeadBucketResult[StatueCode:"+this.statueCode+",Headers:"+this.headers+"]";
	}
}
