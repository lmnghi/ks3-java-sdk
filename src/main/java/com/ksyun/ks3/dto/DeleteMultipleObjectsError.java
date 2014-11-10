package com.ksyun.ks3.dto;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午3:05:01
 * 
 * @description
 **/
public class DeleteMultipleObjectsError {
	private String key;
	private String code;
	private String message;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return "DeleteMultipleObjectsError[key=" + this.key + ",code="
				+ this.code + ",message=" + this.message + "]";
	}
}
