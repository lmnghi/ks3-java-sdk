package com.ksyun.ks3.service.response;

import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 下午2:00:58
 * 
 * @description 
 **/
public class PutObjectResponse extends Ks3WebServiceDefaultResponse<PutObjectResult> implements Md5CheckAble{

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		result = new PutObjectResult();
		result.seteTag(this.getHeader(HttpHeaders.ETag.toString()));
	}

	public String getETag() {
		return this.getHeader(HttpHeaders.ETag.toString());
	}
}
