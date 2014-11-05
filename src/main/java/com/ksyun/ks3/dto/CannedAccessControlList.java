package com.ksyun.ks3.dto;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 上午10:36:46
 * 
 * @description
 **/
public enum CannedAccessControlList {
	Private("private"),
	PublicRead("public-read"), 
	PublicReadWrite("public-read-write");
	private final String cannedAclHeader;

	private CannedAccessControlList(String cannedAclHeader) {
		this.cannedAclHeader = cannedAclHeader;
	}
	public String toString() {
		return cannedAclHeader;
	}
}
