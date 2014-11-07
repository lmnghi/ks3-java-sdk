package com.ksyun.ks3.service.response;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月21日 下午2:17:06
 * 
 * @description 
 **/
public class HeadBucketResponse extends Ks3WebServiceDefaultResponse<Boolean>{

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
/*		int reCode = this.getResponse().getStatusLine().getStatusCode();
		if(reCode==404)
		    this.result = false;
		else if(reCode==200)*/
		this.result = true;
	}

}
