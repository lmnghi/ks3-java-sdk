package com.ksyun.ks3.service.request;

import com.ksyun.ks3.config.Constants;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.between;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月17日 下午2:04:44
 * 
 * @description 分块上传时使用copy
 **/
public class CopyPartRequest extends Ks3WebServiceRequest{
	/**
	 * 数据源bucket
	 */
	private String sourceBucket;
	/**
	 * 数据源object
	 */
	private String sourceObject;
	/**
	 * 从数据源复制的字节范围
	 */
	private long beginRange;
	private long endRange;
	/**
	 * 块顺序
	 */
	private int partNumber;
	/**
	 * 由init multipart获得的uploadId
	 */
	private String uploadId;
	public CopyPartRequest(String sourceBucket,String sourceObject,String destinationBucket,String destinationObject,long beginRange,long endRange,int partNumber,String uploadId){
		super.setBucketname(destinationBucket);
		super.setObjectkey(destinationObject);
		this.sourceBucket = sourceBucket;
		this.sourceObject = sourceObject;
		this.beginRange = beginRange;
		this.endRange = endRange;
		this.partNumber = partNumber;
		this.uploadId = uploadId;
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("partNumber", String.valueOf(this.partNumber));
		this.addParams("uploadId", String.valueOf(this.uploadId));
		this.addHeader(HttpHeaders.XKssCopySource,"/"+ this.sourceBucket+"/"+this.sourceObject);
		this.addHeader(HttpHeaders.XKssCopySourceRange, "bytes="+this.beginRange+"-"+this.endRange);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(sourceBucket))
			throw notNull("sourceBucket");
		if(StringUtils.isBlank(sourceObject))
			throw notNull("sourceObject");
		if(StringUtils.isBlank(this.getBucketname()))
			throw notNull("destinationBucket");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw notNull("destinationObject");
		if(beginRange<0||endRange-beginRange<Constants.minPartSize||endRange-beginRange>Constants.maxPartSize)
	    	throw between("partsize",String.valueOf(endRange-beginRange),String.valueOf(Constants.minPartSize),String.valueOf(Constants.maxPartSize));
		if(partNumber<Constants.minPartNumber||partNumber>Constants.maxPartNumber)
			throw between("partNumber",String.valueOf(partNumber),String.valueOf(Constants.minPartNumber),String.valueOf(Constants.maxPartNumber));
		if(StringUtils.isBlank(uploadId))
			throw notNull("uploadId");

	}

	public String getSourceBucket() {
		return sourceBucket;
	}

	public void setSourceBucket(String sourceBucket) {
		this.sourceBucket = sourceBucket;
	}

	public String getSourceObject() {
		return sourceObject;
	}

	public void setSourceObject(String sourceObject) {
		this.sourceObject = sourceObject;
	}

	public long getBeginRange() {
		return beginRange;
	}

	public void setBeginRange(long beginRange) {
		this.beginRange = beginRange;
	}

	public long getEndRange() {
		return endRange;
	}

	public void setEndRange(long endRange) {
		this.endRange = endRange;
	}

	public int getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

}
