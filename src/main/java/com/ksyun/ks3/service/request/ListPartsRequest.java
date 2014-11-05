package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 下午2:45:35
 * 
 * @description 
 **/
public class ListPartsRequest extends Ks3WebServiceRequest{
	private String uploadId;
	private int maxParts = 1000;
	private int partNumberMarker = -1;
	public ListPartsRequest(String bucketname,String objectkey,String uploadId)
	{
		super.setBucketname(bucketname);
		super.setObjectkey(objectkey);
		this.uploadId = uploadId;
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("max-parts",String.valueOf(this.maxParts));
		this.addParams("uploadId",this.uploadId);
		if(this.partNumberMarker>=0)
		{
			this.addParams("part-number​-marker", String.valueOf(this.partNumberMarker));
		}
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if(StringUtils.isBlank(this.uploadId))
			throw new IllegalArgumentException("uploadId can not be null");
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public int getMaxParts() {
		return maxParts;
	}
	public void setMaxParts(int maxParts) {
		this.maxParts = maxParts;
	}
	public int getPartNumberMarker() {
		return partNumberMarker;
	}
	public void setPartNumberMarker(int partNumberMarker) {
		this.partNumberMarker = partNumberMarker;
	}

}
