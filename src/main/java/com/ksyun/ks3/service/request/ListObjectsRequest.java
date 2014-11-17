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

	/**
	 * 前缀
	 */
	private String prefix;

	/**
	 * 游标
	 */
	private String marker;

	/**
	 * 分隔符
	 */
	private String delimiter;
	/**
	 * 返回的最大数1-1000
	 */
	private Integer maxKeys;
	/**
	 * 要求ks3服务器对返回的结果中object key进行编码
	 */
	private String encodingType;
	/**
	 * 前缀
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * 前缀
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * 游标
	 */
	public String getMarker() {
		return marker;
	}
	/**
	 * 游标
	 */
	public void setMarker(String marker) {
		this.marker = marker;
	}
	/**
	 * 分隔符
	 */
	public String getDelimiter() {
		return delimiter;
	}
	/**
	 * 分隔符
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	/**
	 * 返回的最大数
	 */
	public Integer getMaxKeys() {
		return maxKeys;
	}
	/**
	 * 返回的最大数
	 */
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
			this.addParams("max-keys", String.valueOf(maxKeys));
		if(!StringUtils.isBlank(this.encodingType))
			this.addParams("encoding-type",this.encodingType);
		this.addHeader(HttpHeaders.ContentType, "text/plain");
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException("param bucketName can not be blank");
		if(this.maxKeys!=null&&(this.maxKeys>1000||this.maxKeys<1))
			throw new IllegalArgumentException("maxKeys should between 1 and 1000");
	}
	public String getEncodingType() {
		return encodingType;
	}
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

}
