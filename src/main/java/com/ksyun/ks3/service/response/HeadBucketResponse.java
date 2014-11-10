package com.ksyun.ks3.service.response;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import com.ksyun.ks3.dto.HeadBucketResult;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月21日 下午2:17:06
 * 
 * @description 
 **/
public class HeadBucketResponse extends Ks3WebServiceDefaultResponse<HeadBucketResult>{

	public int[] expectedStatus() {
		return new int[]{200,301,403,404};
	}

	@Override
	public void preHandle() {
		this.result = new HeadBucketResult();
		Header[]  headers = this.getResponse().getAllHeaders();
		for(int i = 0;i< headers.length;i++)
		{
			this.result.getHeaders().put(headers[i].getName(),headers[i].getValue());
		}
		this.result.setStatueCode(this.response.getStatusLine().getStatusCode());
	}

}
