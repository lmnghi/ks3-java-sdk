package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月16日 下午3:30:01
 * 
 * @description <p>
 *              构造函数
 *              </p>
 *              <p>
 *              public ListObjectsRequest(String bucketName)
 *              </p>
 *              <p>
 *              public ListObjectsRequest(String bucketName,String prefix)
 *              </p>
 *              <p>
 *              public ListObjectsRequest(String bucketName, String prefix,
 *              String marker, String delimiter, Integer maxKeys)
 *              </p>
 *              <p>
 *              GET Bucket(List Objects)的请求
 *              </p>
 **/
public class ListObjectsRequest extends Ks3WebServiceRequest {

	/**
	 * prefix和delimiter决定结果中的commonPrefix
	 * <p>
	 * 由prefix和delimiter确定，以prefix开头的object
	 * key,在prefix之后第一次出现delimiter的位置之前（包含delimiter）的子字符串将存在于commonPrefixes中
	 * </p>
	 * <p>
	 * 比如有一下两个object key
	 * </p>
	 * <p>
	 * aaaa/bbb/ddd.txt
	 * </p>
	 * <p>
	 * aaaa/ccc/eee.txt
	 * </p>
	 * <p>
	 * ssss/eee/fff.txt
	 * </p>
	 * <p>
	 * prefix为空 delimiter为/ 则commonPrefix 为 aaaa/和ssss/
	 * </p>
	 * <p>
	 * prefix为aaaa/ delimiter为/ 则commonPrefix 为 aaaa/bbb/和aaaa/ccc/
	 * </p>
	 * <p>
	 * prefix为ssss/ delimiter为/ 则commonPrefix 为 aaaa/eee/
	 * </p>
	 */
	private String prefix;

	/**
	 * 游标
	 */
	private String marker;

	/**
	 * prefix和delimiter决定结果中的commonPrefix
	 * <p>
	 * 由prefix和delimiter确定，以prefix开头的object
	 * key,在prefix之后第一次出现delimiter的位置之前（包含delimiter）的子字符串将存在于commonPrefixes中
	 * </p>
	 * <p>
	 * 比如有一下两个object key
	 * </p>
	 * <p>
	 * aaaa/bbb/ddd.txt
	 * </p>
	 * <p>
	 * aaaa/ccc/eee.txt
	 * </p>
	 * <p>
	 * ssss/eee/fff.txt
	 * </p>
	 * <p>
	 * prefix为空 delimiter为/ 则commonPrefix 为 aaaa/和ssss/
	 * </p>
	 * <p>
	 * prefix为aaaa/ delimiter为/ 则commonPrefix 为 aaaa/bbb/和aaaa/ccc/
	 * </p>
	 * <p>
	 * prefix为ssss/ delimiter为/ 则commonPrefix 为 aaaa/eee/
	 * </p>
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
	public ListObjectsRequest(String bucketName) {
		this(bucketName, null, null, null, null);
	}

	/**
	 * 
	 * @param bucketName
	 * @param prefix
	 */
	public ListObjectsRequest(String bucketName, String prefix) {
		this(bucketName, prefix, null, null, null);
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
		if (!StringUtils.isBlank(this.encodingType))
			this.addParams("encoding-type", this.encodingType);
		this.addHeader(HttpHeaders.ContentType, "text/plain");
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException(
					"param bucketName can not be blank");
		if (this.maxKeys != null && (this.maxKeys > 1000 || this.maxKeys < 1))
			throw new IllegalArgumentException(
					"maxKeys should between 1 and 1000");
	}

	public String getEncodingType() {
		return encodingType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

}
