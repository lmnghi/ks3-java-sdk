package com.ksyun.ks3.dto;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午3:05:01
 * 
 * @description 批量删除object时，删除某单个object时的错误信息
 **/
public class DeleteMultipleObjectsError {
	/**
	 *  object key
	 */
	private String key;
	/**
	 * 错误码，详见readme.txt
	 */
	private String code;
	/**
	 * 错误信息
	 */
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
		return StringUtils.object2string(this);
	}
}
