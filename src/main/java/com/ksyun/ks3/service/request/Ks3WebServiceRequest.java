package com.ksyun.ks3.service.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
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
import com.ksyun.ks3.utils.AuthUtils;
import com.ksyun.ks3.utils.DateUtils;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.Md5Utils;
import com.ksyun.ks3.utils.RequestUtils;

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
	private Authorization auth;
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

	private void initHttpRequestBase() {
		String encodedParams = encodeParams();
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
					entity = new BufferedHttpEntity(entity);
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
				HttpEntity entity = new RepeatableInputStreamRequestEntity(
						requestBody, length);
				// 如果为body为inputstream时，md5值与InputStreamRequestEntity不可兼得（md5
				// 1、直接加密文件 2、流在这个函数结束前必须被读取一次）；代码选择了md5
				if (length == null || Integer.valueOf(length) < 0) {
					try {
						entity = new BufferedHttpEntity(entity);
					} catch (IOException e) {
						e.printStackTrace();
						throw new Ks3ClientException("init http request error("
								+ e + ")", e);
					}
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
		if (this.requestBody instanceof MD5DigestCalculatingInputStream) {
			this.setContentMD5(com.ksyun.ks3.utils.Base64
					.encodeAsString(((MD5DigestCalculatingInputStream) requestBody)
							.getMd5Digest()));
		}
		try {
			this.addHeader(HttpHeaders.Authorization.toString(),
					AuthUtils.calcAuthorization(auth, this));
		} catch (Exception e) {
			throw new Ks3ClientException(
					"calculate user authorization has occured an exception ("
							+ e + ")", e);
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

	private String encodeParams() {
		List<Map.Entry<String, String>> arrayList = new ArrayList<Map.Entry<String, String>>(
				this.params.entrySet());
		Collections.sort(arrayList,
				new Comparator<Map.Entry<String, String>>() {
					public int compare(Entry<String, String> o1,
							Entry<String, String> o2) {
						return Bytes.BYTES_COMPARATOR.compare(o1.getKey()
								.toString().getBytes(), o2.getKey().toString()
								.getBytes());
					}
				});
		List<String> kvList = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : arrayList) {
			if (RequestUtils.subResource.contains(entry.getKey())) {
				if (entry.getValue() != null && !entry.getValue().equals(""))
					kvList.add(entry.getKey() + "=" + entry.getValue());
				else
					kvList.add(entry.getKey());
			}
			if (entry.getValue() != null && !entry.getValue().equals("")) {
				list.add(entry.getKey() + "=" + entry.getValue());
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
		try {
			auth = new Authorization(config.getStr(ClientConfig.ACCESS_KEY_ID),
					config.getStr(ClientConfig.ACCESS_KEY_SECRET));
		} catch (NullPointerException e) {
			throw new Ks3ClientException(
					"please set accessKeyId and accessKeySecret in ClieanConfig");
		}
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
