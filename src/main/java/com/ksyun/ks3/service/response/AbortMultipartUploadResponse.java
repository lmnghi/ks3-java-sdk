package com.ksyun.ks3.service.response;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 下午2:22:36
 * 
 * @description 
 **/
public class AbortMultipartUploadResponse extends Ks3WebServiceDefaultResponse<Boolean>{

	public int[] expectedStatus() {
		return new int[]{204};
	}

	@Override
	public void preHandle() {
		this.result = true;
	}

}
