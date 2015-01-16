package com.ksyun.ks3.dto;

import java.util.Date;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月23日 下午2:34:30
 * 
 * @description 
 **/
public class Part {
	private int partNumber;
	private Date lastModified;
	private String ETag;
	private long size;
	
	@Override
	public String toString()
	{
		return StringUtils.object2string(this);
	}
	
	public int getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getETag() {
		return ETag;
	}
	public void setETag(String eTag) {
		ETag = eTag;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
}
