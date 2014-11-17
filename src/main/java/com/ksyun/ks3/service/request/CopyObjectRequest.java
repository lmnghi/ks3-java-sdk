package com.ksyun.ks3.service.request;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月17日 上午11:19:28
 * 
 * @description Copy Object
 **/
public class CopyObjectRequest extends Ks3WebServiceRequest{
	/**
	 * 源bucket
	 */
	private String sourceBucket;
	/**
	 * 源object
	 */
	private String sourceKey;
	
	
	
	private CannedAccessControlList cannedAcl;
	private AccessControlList accessControlList;
	
	public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey){
		super.setBucketname(destinationBucket);
		super.setObjectkey(destinationObject);
		this.setSourceBucket(sourceBucket);
		this.setSourceKey(sourceKey);
	}
	public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey,CannedAccessControlList cannedAcl){
		this(destinationBucket,destinationObject,sourceBucket,sourceKey);
		this.setCannedAcl(cannedAcl);
	}
	public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey,AccessControlList accessControlList){
		this(destinationBucket,destinationObject,sourceBucket,sourceKey);
		this.setAccessControlList(accessControlList);
	}
	
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addHeader(HttpHeaders.XKssCopySource,"/"+this.getBucketname()+"/"+this.getObjectkey());
        if(getCannedAcl()!=null){
            this.addHeader(HttpHeaders.CannedAcl,getCannedAcl().toString());
        }

        if(this.accessControlList!=null)
        {
            this.getHeader().putAll(HttpUtils.convertAcl2Headers(accessControlList));
        }
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(sourceBucket))
			throw new IllegalArgumentException("sourceBucket can not be null");
		if(StringUtils.isBlank(sourceKey))
			throw new IllegalArgumentException("sourceKey can not be null");
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("destinationBucket can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("destinationObject can not be null");
	}

	public String getSourceBucket() {
		return sourceBucket;
	}

	public void setSourceBucket(String sourceBucket) {
		this.sourceBucket = sourceBucket;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}
	public CannedAccessControlList getCannedAcl() {
		return cannedAcl;
	}

	public void setCannedAcl(CannedAccessControlList cannedAcl) {
		this.cannedAcl = cannedAcl;
	}

	public AccessControlList getAccessControlList() {
		return accessControlList;
	}

	public void setAccessControlList(AccessControlList accessControlList) {
		this.accessControlList = accessControlList;
	}

}
