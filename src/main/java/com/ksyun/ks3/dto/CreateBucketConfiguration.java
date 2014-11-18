package com.ksyun.ks3.dto;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.XmlWriter;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午1:47:26
 * 
 * @description bucket存储地点配置，用于{@link com.ksyun.ks3.service.request.CreateBucketRequest}
 **/
public class CreateBucketConfiguration {
	public static enum REGION {
		BEIJING, HANGZHOU, JIYANG;
		public static REGION load(String s){
			for(REGION region :REGION.values()){
				if(region.toString().equals(s))
					return region;
			}
			throw new Ks3ClientException("unknow region :"+s);
		}
	}
	public CreateBucketConfiguration(REGION region){
		this.location = region;
	}
	private REGION location;

	public REGION getLocation() {
		return location;
	}

	public void setLocation(REGION location) {
		this.location = location;
	}

	public String toString() {
		return StringUtils.object2string(this);
	}
}
