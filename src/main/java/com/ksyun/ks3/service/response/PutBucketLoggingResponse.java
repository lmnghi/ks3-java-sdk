package com.ksyun.ks3.service.response;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description 编辑bucket的日志配置
 **/
public class PutBucketLoggingResponse extends Ks3WebServiceDefaultResponse<Boolean> {

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		this.result = true;
	}

}
