package com.ksyun.ks3.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午6:26:44
 * 
 * @description 用于{@link GetObjectRequest}{@link HeadObjectRequest}
 **/
public class ResponseHeaderOverrides {
	private Map<String,String> overrides = new HashMap<String,String>();
	public void setContentType(String value){
		this.overrides.put("response-content-type", value);
	}
	public void setContentLanguage(String value){
		this.overrides.put("response-content-language", value);
	}
	public void setExpires(String value){
		this.overrides.put("response-expires", value);
	}
	public void setCacheControl(String value){
		this.overrides.put("response-cache-control", value);
	}
	public void setContentDisposition(String value){
		this.overrides.put("response-content-disposition", value);
	}
	public void setContentEncoding(String value){
		this.overrides.put("response-content-encoding", value);
	}
	public Map<String,String> getOverrides()
	{
		return this.overrides;
	}
}
