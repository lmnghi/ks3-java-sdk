package com.ksyun.ks3.service.response;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import com.ksyun.ks3.dto.Bucket;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月15日 下午5:09:30
 * 
 * @description 
 **/
public class CreateBucketResponse extends Ks3WebServiceDefaultResponse<Bucket> {
	public int[] expectedStatus() {
		//TODO 307特殊处理
		return new int[]{200,307};
	}

	@Override
	public void preHandle() {
		this.result = new Bucket();
	}
}
