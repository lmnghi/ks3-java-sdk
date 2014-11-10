package com.ksyun.ks3.dto;

import com.ksyun.ks3.utils.XmlWrite;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午1:47:26
 * 
 * @description
 **/
public class CreateBucketConfiguration {
	public static enum REGION {
		BEIJING, HANGZHOU, JIYANG
	}

	private REGION location;

	public REGION getLocation() {
		return location;
	}

	public void setLocation(REGION location) {
		this.location = location;
	}

	public String toString() {
		return "CreateBucketConfiguration[location=" + this.location + "]";
	}

	public String toXml() {
		return new XmlWrite().startWithNs("CreateBucketConfiguration")
				.start("LocationConstraint").value(this.location.toString())
				.end().end().toString();
	}
}
