package com.ksyun.ks3.service.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.CoreProtocolPNames;

import com.ksyun.ks3.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.http.RepeatableInputStreamRequestEntity;
import com.ksyun.ks3.service.request.support.MD5CalculateAble;
import com.ksyun.ks3.utils.AuthUtils;
import com.ksyun.ks3.utils.DateUtils;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.Md5Utils;
import com.ksyun.ks3.utils.RequestUtils;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午6:10:26
 * 
 * @description
 **/
public abstract class Ks3WebServiceRequest {
	private static final Log log = LogFactory
			.getLog(Ks3WebServiceRequest.class);
	private ClientConfig config = ClientConfig.getConfig();
	private String url;
	private HttpMethod httpMethod;
	private Map<String, String> header = new HashMap<String, String>();
	private Map<String, String> params = new HashMap<String, String>();
	private InputStream requestBody;
	private String paramsToSign = "";
	private String bucketname;
	private String objectkey;
	private HttpRequestBase httpRequest;

	protected void setHeader(Map<String, String> header) {
		this.header = header;
	}

	protected void addHeader(String key, String value) {
		this.header.put(key, value);
	}

	protected void addHeader(HttpHeaders key, String value) {
		this.addHeader(key.toString(), value);
	}

	protected void setRequestBody(InputStream requestBody) {
		this.requestBody = requestBody;
	}

	protected void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	protected void setParams(Map<String, String> params) {
		this.params = params;
	}

	protected void addParams(String key, String value) {
		this.params.put(key, value);
	}

	protected void setContentType(String type) {
		this.header.put(HttpHeaders.ContentType.toString(), type);
	}

	protected void setDate(Date date) {
		this.addHeader(HttpHeaders.Date.toString(), DateUtils.convertDate2Str(
				date, DateUtils.DATETIME_PROTOCOL.RFC1123));
	}

	protected void setContentMD5(String md5) {
		this.addHeader(HttpHeaders.ContentMD5.toString(), md5);
	}

	protected void setBucketname(String bucketname) {
		this.bucketname = bucketname;
	}

	protected void setObjectkey(String object) {
		this.objectkey = object;
	}

	protected void setParamsToSign(String paramsToSign) {
		this.paramsToSign = paramsToSign;
	}

	@SuppressWarnings("deprecation")
	private void initHttpRequestBase() {
		// 准备计算 md5值
		if (this instanceof MD5CalculateAble && this.getRequestBody() != null)
			if (!(this.getRequestBody() instanceof MD5DigestCalculatingInputStream))
				this.setRequestBody(new MD5DigestCalculatingInputStream(this
						.getRequestBody()));

		String encodedParams = encodeParams();
		objectkey = HttpUtils.urlEncode(objectkey, true);
		url = new StringBuffer("http://")
				.append(StringUtils.isBlank(bucketname) ? "" : bucketname + ".")
				.append(url).append("/")
				.append(StringUtils.isBlank(objectkey) ? "" : objectkey)
				.toString();
		if (!StringUtils.isBlank(encodedParams))
			url += "?" + encodedParams;
		HttpRequestBase httpRequest = null;

		if (this.getHttpMethod() == HttpMethod.POST) {
			HttpPost postMethod = new HttpPost(url);
			if (requestBody == null && params != null) {
				try {
					postMethod.setEntity(new StringEntity(encodedParams));
				} catch (UnsupportedEncodingException e) {
					throw new Ks3ClientException(
							"Unable to create HTTP entity:" + e, e);
				}
			} else {
				String length = this.getHeader().get(
						HttpHeaders.ContentLength.toString());
				HttpEntity entity = new RepeatableInputStreamRequestEntity(
						requestBody, length);
				try {
					//这时不能提供content-length,否则 详见BufferedHttpEntity构造函数
					entity = new RepeatableInputStreamRequestEntity(
							requestBody,"-1");
					entity = new BufferedHttpEntity(entity);
					if (this.getRequestBody() instanceof MD5DigestCalculatingInputStream)
						this.addHeader(
								HttpHeaders.ContentMD5,
								com.ksyun.ks3.utils.Base64
										.encodeAsString(((MD5DigestCalculatingInputStream) this
												.getRequestBody())
												.getMd5Digest()));
				} catch (IOException e) {
					e.printStackTrace();
					throw new Ks3ClientException("init http request error(" + e
							+ ")", e);
				}
				postMethod.setEntity(entity);
			}
			httpRequest = postMethod;
		} else if (this.getHttpMethod() == HttpMethod.GET) {
			HttpGet getMethod = new HttpGet(url);
			httpRequest = getMethod;
		} else if (this.getHttpMethod() == HttpMethod.PUT) {
			HttpPut putMethod = new HttpPut(url);
			httpRequest = putMethod;

			putMethod.getParams().setParameter(
					CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
			if (requestBody != null) {
				Map<String, String> headrs = this.getHeader();
				String length = headrs
						.get(HttpHeaders.ContentLength.toString());
				HttpEntity entity = null;
				long availeAble = Runtime.getRuntime().freeMemory()-1024*64;
				if ((length == null || Long.valueOf(length) < availeAble)) {
					try {
						//这时不能提供content-length,否则 详见BufferedHttpEntity构造函数
						entity = new RepeatableInputStreamRequestEntity(
								requestBody,"-1");
						entity = new BufferedHttpEntity(entity);
						if (this.getRequestBody() instanceof MD5DigestCalculatingInputStream)
							this.addHeader(
									HttpHeaders.ContentMD5,
									com.ksyun.ks3.utils.Base64
											.encodeAsString(((MD5DigestCalculatingInputStream) this
													.getRequestBody())
													.getMd5Digest()));
					} catch (IOException e) {
						e.printStackTrace();
						throw new Ks3ClientException("init http request error("
								+ e + ")", e);
					}
				}
				else{
					entity = new RepeatableInputStreamRequestEntity(
							requestBody, length);
				}
				putMethod.setEntity(entity);
			}
		} else if (this.getHttpMethod() == HttpMethod.DELETE) {
			HttpDelete deleteMethod = new HttpDelete(url);
			httpRequest = deleteMethod;
		} else if (this.getHttpMethod() == HttpMethod.HEAD) {
			HttpHead headMethod = new HttpHead(url);
			httpRequest = headMethod;
		} else {
			throw new Ks3ClientException("Unknow http method : "
					+ this.getHttpMethod());
		}
		for (Entry<String, String> aHeader : header.entrySet()) {
			if (!httpRequest.containsHeader(aHeader.getKey()))
				httpRequest.addHeader(aHeader.getKey(), aHeader.getValue());
		}
		this.httpRequest = httpRequest;
		this.httpRequest.removeHeaders(HttpHeaders.ContentLength.toString());
	}

	public HttpRequestBase getHttpRequest() {
		try {
			this.validateParams();
			configHttpRequestPrivate();
			configHttpRequest();
			initHttpRequestBase();
		} finally {
			/*
			 * if(this.requestBody!=null) try { this.requestBody.close(); }
			 * catch (IOException e) { e.printStackTrace();
			 * log.info("can not close request body input stream"); }
			 */
		}
		return this.httpRequest;
	}

	@SuppressWarnings("deprecation")
	private String encodeParams() {
		List<Map.Entry<String, String>> arrayList = new ArrayList<Map.Entry<String, String>>(
				this.params.entrySet());
		Collections.sort(arrayList,
				new Comparator<Map.Entry<String, String>>() {
					public int compare(Entry<String, String> o1,
							Entry<String, String> o2) {
						return o1.getKey().compareTo(o2.getKey());
					}
				});
		List<String> kvList = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : arrayList) {
			String value = null;
			if(!StringUtils.isBlank(entry.getValue()))
			    value = URLEncoder.encode(entry.getValue());
			if (RequestUtils.subResource.contains(entry.getKey())) {
				if (value != null && !value.equals(""))
					kvList.add(entry.getKey() + "=" + value);
				else
					kvList.add(entry.getKey());
			}
			if (value != null && !value.equals("")) {
				list.add(entry.getKey() + "=" + value);
			} else
				list.add(entry.getKey());
		}

		String queryParams = StringUtils.join(list.toArray(), "&");
		this.setParamsToSign(StringUtils.join(kvList.toArray(), "&"));
		return queryParams;
	}

	private void configHttpRequestPrivate() {
		url = ClientConfig.getConfig().getStr(ClientConfig.END_POINT);
		if (url.startsWith("http://") || url.startsWith("https://"))
			url = url.replace("http://", "").replace("https://", "");
		httpMethod = HttpMethod.POST;
		this.setContentMD5("");
		this.setContentType("text/plain");
		this.setDate(new Date());
	}

	protected abstract void configHttpRequest();

	protected abstract void validateParams() throws IllegalArgumentException;

	// getters
	protected String getUrl() {
		return url;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	protected InputStream getRequestBody() {
		return requestBody;
	}

	public HttpMethod getHttpMethod() {
		return this.httpMethod;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getContentMD5() {
		return this.header.get(HttpHeaders.ContentMD5.toString());
	}

	public String getContentType() {
		return this.header.get(HttpHeaders.ContentType.toString());
	}

	public Date getDate() {
		String s = this.header.get(HttpHeaders.Date.toString());
		if (StringUtils.isBlank(s)) {
			return null;
		} else {
			return DateUtils.convertStr2Date(s,
					DateUtils.DATETIME_PROTOCOL.RFC1123);
		}

	}

	public String getParamsToSign() {
		return paramsToSign;
	}

	public String getBucketname() {
		return bucketname;
	}

	public String getObjectkey() {
		return objectkey;
	}
}
