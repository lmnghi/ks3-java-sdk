package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description 列出bucket下未进行complete或abort的分块上传.该分块上传的块依然占有用户的存储空间，
 *              可以隔一段时间将bucket下未complete或abort的分块上传进行complete或abort操作
 **/
public class ListMultipartUploadsRequest extends Ks3WebServiceRequest {
	/**
	 * prefix和delimiter决定结果中的commonPrefix
	 * <p>由prefix和delimiter确定，以prefix开头的object key,在prefix之后第一次出现delimiter的位置之前（包含delimiter）的子字符串将存在于commonPrefixes中</p>
	 * <p>比如有一下两个object key</p>
	 * <p>aaaa/bbb/ddd.txt</p>
	 * <p>aaaa/ccc/eee.txt</p>
	 * <p>ssss/eee/fff.txt</p>
	 * <p>prefix为空 delimiter为/ 则commonPrefix 为 aaaa/和ssss/</p>
	 * <p>prefix为aaaa/  delimiter为/ 则commonPrefix 为 aaaa/bbb/和aaaa/ccc/</p>
	 * <p>prefix为ssss/  delimiter为/ 则commonPrefix 为 aaaa/eee/</p>
	 */
	private String delimiter;
	/**
	 * <p>
	 * keyMarker为空，uploadIdMarker不为空
	 * <P>
	 * <p>
	 * 无意义
	 * </p>
	 * <p>
	 * keyMarker不为空，uploadIdMarker不为空
	 * <P>
	 * <p>
	 * 列出分块上传object key为keyMarker，且upload id 大于uploadIdMarker的结果
	 * </p>
	 * <p>
	 * keyMarker不为空，uploadIdMarker为空
	 * <P>
	 * <p>
	 * 列出分块上传object key大于keyMarker的结果
	 * </p>
	 */
	private String keyMarker;
	private String uploadIdMarker;
	/**
	 * 设置返回结果数，1-1000
	 */
	private Integer maxUploads;
	/**
	 * prefix和delimiter决定结果中的commonPrefix
	 * <p>由prefix和delimiter确定，以prefix开头的object key,在prefix之后第一次出现delimiter的位置之前（包含delimiter）的子字符串将存在于commonPrefixes中</p>
	 * <p>比如有一下两个object key</p>
	 * <p>aaaa/bbb/ddd.txt</p>
	 * <p>aaaa/ccc/eee.txt</p>
	 * <p>ssss/eee/fff.txt</p>
	 * <p>prefix为空 delimiter为/ 则commonPrefix 为 aaaa/和ssss/</p>
	 * <p>prefix为aaaa/  delimiter为/ 则commonPrefix 为 aaaa/bbb/和aaaa/ccc/</p>
	 * <p>prefix为ssss/  delimiter为/ 则commonPrefix 为 aaaa/eee/</p>
	 */
	private String prefix;
	/**
	 * 要求ks3服务器对返回的结果中object key进行编码
	 */
	private String encodingType;

	public ListMultipartUploadsRequest(String bucketName) {
		super.setBucketname(bucketName);
	}

	public ListMultipartUploadsRequest(String bucketName, String prefix) {
		super.setBucketname(bucketName);
		this.setPrefix(prefix);
	}

	public ListMultipartUploadsRequest(String bucketName, String prefix,
			String keyMarker, String uploadIdMarker) {
		super.setBucketname(bucketName);
		this.setPrefix(prefix);
		this.setKeyMarker(keyMarker);
		this.setUploadIdMarker(uploadIdMarker);
	}

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		this.addParams("uploads", null);
		this.addParams("prefix", prefix);
		this.addParams("key-marker", this.keyMarker);
		this.addParams("upload-id-​marker", this.uploadIdMarker);
		this.addParams("delimiter", delimiter);
		if (this.maxUploads != null)
			this.addParams("max-uploads", String.valueOf(maxUploads));
		if (!StringUtils.isBlank(this.encodingType))
			this.addParams("encoding-type", this.encodingType);

	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException(
					"param bucketName can not be blank");
		if (this.maxUploads != null
				&& (this.maxUploads > 1000 || this.maxUploads < 1))
			throw new IllegalArgumentException(
					"maxUploads should between 1 and 1000");
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
