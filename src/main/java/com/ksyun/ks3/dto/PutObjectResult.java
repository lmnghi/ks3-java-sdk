package com.ksyun.ks3.dto;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 下午1:54:37
 * 
 * @description PUT Object返回的结果
 **/
public class PutObjectResult {
	private String eTag;
	public String toString()
	{
		return StringUtils.object2string(this);
	}
	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
}
