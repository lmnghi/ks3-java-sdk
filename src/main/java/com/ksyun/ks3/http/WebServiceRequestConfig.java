package com.ksyun.ks3.http;

import com.ksyun.ks3.config.Constants;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月17日 下午5:43:06
 * 
 * @description 
 **/
public class WebServiceRequestConfig {
	private String userAgent = Constants.KS3_SDK_USER_AGENT;

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
