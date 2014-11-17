package com.ksyun.ks3.dto;

import com.ksyun.ks3.dto.CreateBucketConfiguration.REGION;
import com.ksyun.ks3.utils.XmlWriter;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月11日 下午1:35:29
 * 
 * @description 
 **/
public class CreateBucketConfigurationForTest extends CreateBucketConfiguration{
	public CreateBucketConfigurationForTest(
			com.ksyun.ks3.dto.CreateBucketConfiguration.REGION region) {
		super(region);
	}

	public static enum REGION {
		BEIJING, HANGZHOU, JIYANG,TAIYUAN
	}
	private com.ksyun.ks3.dto.CreateBucketConfiguration.REGION location;

	public com.ksyun.ks3.dto.CreateBucketConfiguration.REGION getLocation() {
		return location;
	}

	public void setLocation(com.ksyun.ks3.dto.CreateBucketConfiguration.REGION location) {
		this.location = location;
	}

	public String toString() {
		return "CreateBucketConfiguration[location=" + this.location + "]";
	}

	public String toXml() {
		return new XmlWriter().startWithNs("CreateBucketConfiguration")
				.start("LocationConstraint").value(this.location.toString())
				.end().end().toString();
	}
}
