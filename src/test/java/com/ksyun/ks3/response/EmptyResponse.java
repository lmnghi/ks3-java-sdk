package com.ksyun.ks3.response;

import com.ksyun.ks3.service.response.Ks3WebServiceDefaultResponse;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月11日 上午10:54:15
 * 
 * @description 
 **/
public class EmptyResponse extends Ks3WebServiceDefaultResponse<Boolean>{

	public int[] expectedStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void preHandle() {
		// TODO Auto-generated method stub
		
	}

}
