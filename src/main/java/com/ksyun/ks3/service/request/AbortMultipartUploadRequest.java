package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月23日 下午2:17:56
 * 
 * @description 取消分块上传操作
 **/
public class AbortMultipartUploadRequest extends Ks3WebServiceRequest{
	/**
	 * 通过Init Multipart Upload 初始化得到的uploadId
	 */
	private String uploadId;
	/**
	 * 
	 * @param bucketname
	 * @param objectkey
	 * @param uploadId
	 */
	public AbortMultipartUploadRequest(String bucketname,String objectkey,String uploadId)
	{
		super.setBucketname(bucketname);
		super.setObjectkey(objectkey);
		this.setUploadId(uploadId);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.DELETE);
		this.addParams("uploadId",this.uploadId);
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
	/**
	 * 通过Init Multipart Upload 初始化得到的uploadId
	 */
	public String getUploadId() {
		return uploadId;
	}
	/**
	 * 通过Init Multipart Upload 初始化得到的uploadId
	 */
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

}
