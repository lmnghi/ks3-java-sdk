package com.ksyun.ks3.dto;

import java.util.List;

import com.ksyun.ks3.utils.StringUtils;


/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月31日 上午10:59:03
 * 
 * @description 跨域资源共享规则
 **/
public class CorsRule {
	//指定允许的跨域请求方法,必须至少指定一种方法
	private List<AllowedMethods> allowedMethods;
	//指定允许跨域请求的来源 ，必须至少指定一个。可以使用通配符 *，但是一个值中最多能包含一个*
	private List<String> allowedOrigins;
	//指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒。 
	private int maxAgeSeconds;
	//指定允许用户从应用程序中访问的响应头 
	private List<String> exposedHeaders;
	//控制在 OPTIONS 预取指令中 Access-Control-Request-Headers 头中指定的 header 是否允许。可以使用通配符 *，但是一个值中最多能包含一个*
	private List<String> allowedHeaders;

	@Override
	public String toString(){
		return StringUtils.object2string(this);
	}

	public List<AllowedMethods> getAllowedMethods() {
		return allowedMethods;
	}


	public void setAllowedMethods(List<AllowedMethods> allowedMethods) {
		this.allowedMethods = allowedMethods;
	}


	public List<String> getAllowedOrigins() {
		return allowedOrigins;
	}


	public void setAllowedOrigins(List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}


	public int getMaxAgeSeconds() {
		return maxAgeSeconds;
	}


	public void setMaxAgeSeconds(int maxAgeSeconds) {
		this.maxAgeSeconds = maxAgeSeconds;
	}


	public List<String> getExposedHeaders() {
		return exposedHeaders;
	}


	public void setExposedHeaders(List<String> exposedHeaders) {
		this.exposedHeaders = exposedHeaders;
	}


	public List<String> getAllowedHeaders() {
		return allowedHeaders;
	}


	public void setAllowedHeaders(List<String> allowedHeaders) {
		this.allowedHeaders = allowedHeaders;
	}


	public static enum AllowedMethods {
		GET, PUT, HEAD, POST, DELETE;
		public static AllowedMethods load(String s){
			for(AllowedMethods e:AllowedMethods.values()){
				if(e.toString().equals(s))
					return e;
			}
			return null;
		}
	}
}
