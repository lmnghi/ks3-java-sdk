package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月23日 下午2:45:35
 * 
 * @description 列出某个uploadid下已上传的块
 **/
public class ListPartsRequest extends Ks3WebServiceRequest{
	/**
	 * 由init multipart upload 获取到的upload id
	 */
	private String uploadId;
	/**
	 * 列出的最大数，1-1000
	 */
	private Integer maxParts = 1000;
	/**
	 * 游标，将列出partNumber比该值大的part
	 */
	private Integer partNumberMarker = -1;
	/**
	 * 要求Ks3服务器对返回结果的objectkey进行编码
	 */
	private String encodingType;
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
		if(partNumberMarker!=null&&this.partNumberMarker>=0)
		{
			this.addParams("part-number​-marker", String.valueOf(this.partNumberMarker));
		}
		if(!StringUtils.isBlank(this.encodingType))
			this.addParams("encoding-type",this.encodingType);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if(StringUtils.isBlank(this.uploadId))
			throw new IllegalArgumentException("uploadId can not be null");
		if(this.maxParts!=null&&(this.maxParts>1000||this.maxParts<1))
			throw new IllegalArgumentException("maxParts should between 1 and 1000");
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public Integer getMaxParts() {
		return maxParts;
	}
	public void setMaxParts(Integer maxParts) {
		this.maxParts = maxParts;
	}
	public Integer getPartNumberMarker() {
		return partNumberMarker;
	}
	public void setPartNumberMarker(Integer partNumberMarker) {
		this.partNumberMarker = partNumberMarker;
	}
	public String getEncodingType() {
		return encodingType;
	}
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	
}
