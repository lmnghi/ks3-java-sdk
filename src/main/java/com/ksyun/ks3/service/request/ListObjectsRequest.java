package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:30:01
 * 
 * @description
 **/
public class ListObjectsRequest extends Ks3WebServiceRequest {

	private String prefix;

	private String marker;

	private String delimiter;

	private Integer maxKeys;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public Integer getMaxKeys() {
		return maxKeys;
	}

	public void setMaxKeys(Integer maxKeys) {
		this.maxKeys = maxKeys;
	}
	/**
	 * 
	 * @param bucketName
	 */
	public ListObjectsRequest(String bucketName){
		this(bucketName,null,null,null,null);
	}
	/**
	 * 
	 * @param bucketName
	 * @param prefix
	 */
	public ListObjectsRequest(String bucketName,String prefix)
	{
		this(bucketName,prefix,null,null,null);
	}
	/**
	 * 
	 * @param bucketName
	 * @param prefix
	 * @param marker
	 * @param delimiter
	 * @param maxKeys
	 */
	public ListObjectsRequest(String bucketName, String prefix, String marker,
			String delimiter, Integer maxKeys) {
		setBucketname(bucketName);
		this.prefix = prefix;
		this.marker = marker;
		if (StringUtils.isBlank(delimiter)) {
			this.delimiter = "/";
		} else {
			this.delimiter = delimiter;
		}
		this.maxKeys = maxKeys;
	}

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);

		this.addParams("prefix", prefix);
		this.addParams("marker", marker);
		this.addParams("delimiter", delimiter);
		if (maxKeys != null)
			this.addParams("max-keys", maxKeys.toString());
		this.addHeader(HttpHeaders.ContentType, "text/plain");
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException("param bucketName can not be blank");
	}

}
