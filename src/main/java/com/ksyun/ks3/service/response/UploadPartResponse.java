package com.ksyun.ks3.service.response;

import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.service.response.support.Md5CheckAble;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 上午11:27:48
 * 
 * @description 
 **/
public class UploadPartResponse extends Ks3WebServiceDefaultResponse<PartETag> implements Md5CheckAble{

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		result = new PartETag();
		result.seteTag(this.getHeader(HttpHeaders.ETag.toString()));
	}
	public String getETag() {
		return this.getHeader(HttpHeaders.ETag.toString());
	}
}
