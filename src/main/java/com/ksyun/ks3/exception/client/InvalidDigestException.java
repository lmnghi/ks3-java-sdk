package com.ksyun.ks3.exception.client;

import com.ksyun.ks3.exception.Ks3ClientException;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月19日 下午3:00:15
 * 
 * @description 客户端MD5校验出错
 **/
public class InvalidDigestException extends Ks3ClientException{

	public InvalidDigestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
