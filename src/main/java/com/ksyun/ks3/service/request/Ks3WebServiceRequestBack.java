package com.ksyun.ks3.service.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.ksyun.ks3.RepeatableFileInputStream;
import com.ksyun.ks3.RepeatableInputStream;
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
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月14日 下午6:10:26
 * 
 * @description
 **/
public abstract class Ks3WebServiceRequestBack {
	private static final Log log = LogFactory
			.getLog(Ks3WebServiceRequest.class);
	private String url;
	private HttpMethod httpMethod;
	private Map<String, String> header = new HashMap<String, String>();
	private Map<String, String> params = new HashMap<String, String>();
	private InputStream requestBody;
	private String bucketname;
	private String objectkey;
	//由于查询数据处理任务那个接口特殊而加的一个东西
	private String pathVariable;
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

	public void setRequestBody(InputStream requestBody) {
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
	protected void setPathVariable(String pathVariable) {
		this.pathVariable = pathVariable;
	}
	@SuppressWarnings("deprecation")
	private void initHttpRequestBase() {
		if(this.getRequestBody()!=null){
			if(!(this.requestBody instanceof RepeatableInputStream)&&!(this.requestBody instanceof RepeatableFileInputStream)){
				this.setRequestBody(new RepeatableInputStream(this.requestBody,Constants.DEFAULT_STREAM_BUFFER_SIZE));
			}
		}
		// 准备计算 md5值
		if (this instanceof MD5CalculateAble && this.getRequestBody() != null
				&& StringUtils.isBlank(this.getContentMD5())
				&&!((MD5CalculateAble)this).skipCal())
			if (!(this.getRequestBody() instanceof MD5DigestCalculatingInputStream))
				this.setRequestBody(new MD5DigestCalculatingInputStream(this
						.getRequestBody()));
		String _objectkey = null;
		String encodedParams = HttpUtils.encodeParams(getParams());
		_objectkey = HttpUtils.urlEncode(objectkey, true);
		int format = ClientConfig.getConfig().getInt(
				ClientConfig.CLIENT_URLFORMAT);
		String protocol = ClientConfig.getConfig().getStr(ClientConfig.HTTP_PROTOCOL);
		if (format == 0) {
			url = new StringBuffer(protocol+"://")
					.append(StringUtils.isBlank(bucketname) ? "" : bucketname
							+ ".").append(url)
					.append(StringUtils.isBlank(pathVariable) ? "" :"/"+ pathVariable)
					.append(StringUtils.isBlank(_objectkey) ? "" :"/"+ _objectkey)
					.toString();
		} else {
			url = new StringBuffer(protocol+"://")
					.append(url)
					.append(StringUtils.isBlank(bucketname) ? "" :"/" +bucketname)
					.append(StringUtils.isBlank(pathVariable) ? "" :"/"+ pathVariable)
					.append(StringUtils.isBlank(_objectkey) ? "" : "/"+_objectkey)
					.toString();
		}
		if (!StringUtils.isBlank(encodedParams))
			url += "?" + encodedParams;
		HttpRequestBase httpRequest = null;
		if (this.getHttpMethod() == HttpMethod.POST) {
			HttpPost postMethod = new HttpPost(url);
			if (requestBody == null && params != null) {
				//之前会把参数进行编码放在body中，之后发现这么做没啥用
			} else {
				String length = this.getHeader().get(
						HttpHeaders.ContentLength.toString());
				HttpEntity entity = new RepeatableInputStreamRequestEntity(
						requestBody, length);
				try {
					// 这时不能提供content-length,否则 详见BufferedHttpEntity构造函数
					entity = new RepeatableInputStreamRequestEntity(
							requestBody, "-1");
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
					throw new Ks3ClientException(
							"初始化Http Request出错(" + e + ")", e);
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
				if (length == null||length.trim().equals("0")) {
					try {
						entity = new RepeatableInputStreamRequestEntity(
								requestBody, "-1");
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
						throw new Ks3ClientException("初始化Http Request出错(" + e
								+ ")", e);
					}
				} else {
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
			// 永远不会到这儿
			throw new Ks3ClientException("Unknow http method : "
					+ this.getHttpMethod());
		}
		for (Entry<String, String> aHeader : header.entrySet()) {
			if (!httpRequest.containsHeader(aHeader.getKey()))
				httpRequest.setHeader(aHeader.getKey(), aHeader.getValue());
		}
		this.httpRequest = httpRequest;
		// 添加长度会报错，最后Apache http框架会自动添加
		this.httpRequest.removeHeaders(HttpHeaders.ContentLength.toString());
	}

	public HttpRequestBase getHttpRequest() {
		reset();
		this.validateParams();
		configHttpRequestPrivate();
		configHttpRequest();
		initHttpRequestBase();
		return this.httpRequest;
	}
	private void configHttpRequestPrivate() {
		url = ClientConfig.getConfig().getStr(ClientConfig.END_POINT);
		if (url.startsWith("http://") || url.startsWith("https://"))
			url = url.replace("http://", "").replace("https://", "");
		httpMethod = HttpMethod.POST;
		if(!this.getHeader().containsKey(HttpHeaders.UserAgent.toString()))
			this.addHeader(HttpHeaders.UserAgent, Constants.KS3_SDK_USER_AGENT);
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

	public InputStream getRequestBody() {
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

	public String getBucketname() {
		return bucketname;
	}

	public String getObjectkey() {
		return objectkey;
	}
	private void reset(){
		if(this.header!=null)
			this.header.clear();
		this.httpRequest=null;
		if(this.params!=null)
			this.params.clear();
	}	
}
