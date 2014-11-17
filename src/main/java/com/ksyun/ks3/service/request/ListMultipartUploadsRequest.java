package com.ksyun.ks3.service.request;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description 列出bucket下未进行complete或abort的分块上传的块
 **/
public class ListMultipartUploadsRequest extends Ks3WebServiceRequest{
	/**
	 * 分隔符
	 */
	private String delimiter = "/";
	/**
	 * <p>keyMarker为空，uploadIdMarker不为空<P>
	 * <p>无意义</p>
	 * <p>keyMarker不为空，uploadIdMarker不为空<P>
	 * <p>列出分块上传object key为keyMarker，且upload id 大于uploadIdMarker的块</p>
	 * <p>keyMarker不为空，uploadIdMarker为空<P>
	 * <p>列出分块上传object key大于keyMarker的块</p>
	 */
	private String keyMarker;
	private String uploadIdMarker;
	/**
	 * 设置返回结果数，1-1000
	 */
	private Integer maxUploads;
	/**
	 * object key 前缀
	 */
	private String prefix;
	/**
	 * 要求ks3服务器对返回的结果中object key进行编码
	 */
	private String encodingType;
	public ListMultipartUploadsRequest(String bucketName){
		super.setBucketname(bucketName);
	}
	public ListMultipartUploadsRequest(String bucketName,String prefix){
		super.setBucketname(bucketName);
		this.setPrefix(prefix);
	}
	public ListMultipartUploadsRequest(String bucketName,String prefix,String keyMarker,String uploadIdMarker){
		super.setBucketname(bucketName);
		this.setPrefix(prefix);
		this.setKeyMarker(keyMarker);
		this.setUploadIdMarker(uploadIdMarker);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("uploads",null);
		this.addParams("prefix", prefix);
		this.addParams("key-marker", this.keyMarker);
		this.addParams("upload-id-​marker", this.uploadIdMarker);
		this.addParams("delimiter", delimiter);
		if (this.maxUploads != null)
			this.addParams("max-uploads", String.valueOf(maxUploads));
		if(!StringUtils.isBlank(this.encodingType))
			this.addParams("encoding-type",this.encodingType);
		
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException("param bucketName can not be blank");
		if(this.maxUploads!=null&&(this.maxUploads>1000||this.maxUploads<1))
			throw new IllegalArgumentException("maxUploads should between 1 and 1000");
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getKeyMarker() {
		return keyMarker;
	}

	public void setKeyMarker(String keyMarker) {
		this.keyMarker = keyMarker;
	}

	public String getUploadIdMarker() {
		return uploadIdMarker;
	}

	public void setUploadIdMarker(String uploadIdMarker) {
		this.uploadIdMarker = uploadIdMarker;
	}

	public Integer getMaxUploads() {
		return maxUploads;
	}

	public void setMaxUploads(Integer maxUploads) {
		this.maxUploads = maxUploads;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	
}
