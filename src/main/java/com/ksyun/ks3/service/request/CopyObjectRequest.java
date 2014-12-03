package com.ksyun.ks3.service.request;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.StringUtils;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;


/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月17日 上午11:19:28
 * 
 * @description Copy Object
 * <p>将指定的object复制到目标地点，将复制源object的元数据、acl等信息</p>
 * <p>public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey)</p>
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
	
	/**
	 * 通过CannedAccessControlList设置新的object的acl
	 */
	private CannedAccessControlList cannedAcl;
	/**
	 * 设置新的object的acl
	 */
	private AccessControlList accessControlList;
	/**
	 * 
	 * @param destinationBucket 目标bucket
	 * @param destinationObject 目标object
	 * @param sourceBucket 源bucket
	 * @param sourceKey 源object
	 */
	public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey){
		super.setBucketname(destinationBucket);
		super.setObjectkey(destinationObject);
		this.setSourceBucket(sourceBucket);
		this.setSourceKey(sourceKey);
	}
	/**
	 * 
	 * @param destinationBucket 目标bucket
	 * @param destinationObject 目标object
	 * @param sourceBucket 源bucket
	 * @param sourceKey 源bucket
	 * @param cannedAcl 设置新object的acl
	 */
	public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey,CannedAccessControlList cannedAcl){
		this(destinationBucket,destinationObject,sourceBucket,sourceKey);
		this.setCannedAcl(cannedAcl);
	}
	/**
	 * 
	 * @param destinationBucket 目标bucket
	 * @param destinationObject 目标object
	 * @param sourceBucket 源bucket
	 * @param sourceKey 源bucket
	 * @param accessControlList 设置新object的acl
	 */
	public CopyObjectRequest(String destinationBucket,String destinationObject,String sourceBucket,String sourceKey,AccessControlList accessControlList){
		this(destinationBucket,destinationObject,sourceBucket,sourceKey);
		this.setAccessControlList(accessControlList);
	}
	
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addHeader(HttpHeaders.XKssCopySource,"/"+this.getSourceBucket()+"/"+this.getSourceKey());
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
			throw notNull("sourceBucket");
		if(StringUtils.isBlank(sourceKey))
			throw notNull("sourceKey");
		if(StringUtils.isBlank(this.getBucketname()))
			throw notNull("destinationBucket");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw notNull("destinationObject");
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
