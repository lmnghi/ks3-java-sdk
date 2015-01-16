package com.ksyun.ks3.service.request.support;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月6日 下午4:29:01
 * 
 * @description 表示这个请求在请求结束之后会将body之中的内容进行md5编码。一般只用于PUT
 **/
public interface MD5CalculateAble {
	public String getMd5();
}
