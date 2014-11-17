package com.ksyun.ks3.dto;

import java.util.Date;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description
 **/
public class MultiPartInfo {
	private String key;
	private String uploadId;
	private String storageClass;
	private Date initiated;
	private Owner initiator;
	private Owner owner;

	@Override
	public String toString() {
		return "MultiPartInfo[key=" + this.key + ";uploadid=" + this.uploadId
				+ ";storageClass=" + this.storageClass + ";initiated="
				+ this.initiated + ";owner=" + this.owner + ";initiator="
				+ this.initiator + "]";
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
