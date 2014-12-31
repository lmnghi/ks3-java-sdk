package com.ksyun.ks3.service.response;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月31日 上午11:50:13
 * 
 * @description 
 **/
public class PutBucketCorsResponse extends Ks3WebServiceDefaultResponse<Boolean>{

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		this.result = true;
	}

}
