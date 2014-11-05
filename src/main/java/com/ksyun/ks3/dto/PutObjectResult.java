package com.ksyun.ks3.dto;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 下午1:54:37
 * 
 * @description 
 **/
public class PutObjectResult {
	private String eTag;
	public String toString()
	{
		return "PutObjectResult[ETag="+this.eTag+"]";
	}
	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	
}
