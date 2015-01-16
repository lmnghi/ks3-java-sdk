package com.ksyun.ks3.dto;

import java.util.Date;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description list mutipart uploads操作返回的结果
 **/
public class MultiPartUploadInfo {
	private String key;
	private String uploadId;
	/**
	 * object 存储方式（STANDARD）
	 */
	private String storageClass;
	/**
	 * init multipart upload的时间
	 */
	private Date initiated;
	/**
	 * init multipart upload的用户
	 */
	private Owner initiator;
	/**
	 * 这个object的所有者
	 */
	private Owner owner;

	@Override
	public String toString() {
		return StringUtils.object2string(this);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getStorageClass() {
		return storageClass;
	}

	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}

	public Date getInitiated() {
		return initiated;
	}

	public void setInitiated(Date initiated) {
		this.initiated = initiated;
	}

	public Owner getInitiator() {
		return initiator;
	}

	public void setInitiator(Owner initiator) {
		this.initiator = initiator;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

}
