package com.ksyun.ks3.dto;

import java.util.Date;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月16日 下午3:23:58
 * 
 * @description 
 **/
public class Ks3ObjectSummary {
	/**
	 * bucket名称
	 */
	protected String bucketName;
	/**
	 * object key
	 */
	protected String key;
	protected String eTag;
	/**
	 * 该object的大小，（字节数）
	 */
	protected long size;
	/**
	 * 上次修改时间
	 */
	protected Date lastModified;
	/**
	 * 存储类型，目前支持STANDARD
	 */
	protected String storageClass;
	/**
	 * object拥有者
	 */
	protected Owner owner;

	public String toString() {
		return StringUtils.object2string(this);
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getStorageClass() {
		return storageClass;
	}

	public void setStorageClass(String storageClass) {
		this.storageClass = storageClass;
	}
}
