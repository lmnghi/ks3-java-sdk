package com.ksyun.ks3.service.response;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月16日 下午3:11:30
 * 
 * @description 
 **/
public class DeleteBucketResponse extends Ks3WebServiceDefaultResponse<Boolean>{

	public int[] expectedStatus() {
		return new int[]{204};
	}

	@Override
	public void preHandle() {	
		this.result = true;
	}
}
