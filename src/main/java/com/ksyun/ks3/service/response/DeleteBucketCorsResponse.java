package com.ksyun.ks3.service.response;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月31日 下午3:39:26
 * 
 * @description 
 **/
public class DeleteBucketCorsResponse extends Ks3WebServiceDefaultResponse<Boolean>{

	public int[] expectedStatus() {
		//200是为了兼容现在的ks3 api正确应该是204
		return new int[]{200,204};
	}

	@Override
	public void preHandle() {
		this.result = true;
	}

}
