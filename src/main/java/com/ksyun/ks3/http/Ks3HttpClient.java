package com.ksyun.ks3.http;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.service.response.Ks3WebServiceResponse;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.Timer;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午6:59:38
 * 
 * @description ks3 sdk Ks3WebServiceRequest执行，K3WebServiceResponse解析核心控制器
 **/
public class Ks3HttpClient {
	private static final Log log = LogFactory.getLog(Ks3HttpClient.class);
	private HttpClient client = new HttpClientFactory().createHttpClient();

	public <X extends Ks3WebServiceResponse<Y>, Y> Y execute(
			Ks3WebServiceRequest request, Class<X> clazz) {
		try {
			ClientConfig config = ClientConfig.getConfig();
			if (StringUtils.isBlank(config.getStr(ClientConfig.ACCESS_KEY_ID))
					|| StringUtils.isBlank(config
							.getStr(ClientConfig.ACCESS_KEY_SECRET)))
				throw new Ks3ClientException(
						"client not login!AccessKeyId or AccessKeySecret is blank");
			if (request == null || clazz == null)
				throw new IllegalArgumentException();
			Y result = doExecute(request, clazz);
			return result;
		} catch (Exception e) {
			// 异常格式转化统一
			if (e instanceof Ks3ServiceException) {
				log.error(e);
				e.printStackTrace();
				throw (Ks3ServiceException) e;
			} else if (e instanceof Ks3ClientException) {
				log.error(e);
				e.printStackTrace();
				throw (Ks3ClientException) e;
			} else {
				Ks3ClientException exception = new Ks3ClientException(e);
				log.error(exception);
				exception.printStackTrace();
				throw exception;
			}
		} finally {
			ClientConfig config = ClientConfig.getConfig();
			// 客户端模式为1的话自动清理accesskeyid和accesskeysecret
			if ("1".equals(config.getStr(ClientConfig.CLIENT_MODEL))) {
				config.set(ClientConfig.ACCESS_KEY_ID, null);
				config.set(ClientConfig.ACCESS_KEY_SECRET, null);
			}
			System.out.println();
		}
	}

	private <X extends Ks3WebServiceResponse<Y>, Y> Y doExecute(
			Ks3WebServiceRequest request, Class<X> clazz) {
		Timer.start();
		HttpResponse response = null;
		HttpRequestBase httpRequest = request.getHttpRequest();
		log.info("finished convert httprequest : " + Timer.end());
		try {
			response = client.execute(httpRequest);
			log.info("finished send request to ks3 service and recive response from the service : "
					+ Timer.end());
			doLogHttp(httpRequest, response);
			log.info("finished log request and response : " + Timer.end());
		} catch (ClientProtocolException e) {
			log.error(e);
			httpRequest.abort();
			throw new Ks3ClientException(
					"Request to Ks3 has occured an ClientProtocolException:("
							+ e + ")", e);
		} catch (IOException e) {
			httpRequest.abort();
			log.error(e);
			throw new Ks3ClientException(
					"Request to Ks3 has occured an IO exception:(" + e + ")", e);
		} catch (Exception e) {
			httpRequest.abort();
			log.error(e);
			throw new Ks3ClientException(
					"Request to Ks3 has occured an exception:(" + e + ")", e);
		}
		Ks3WebServiceResponse<Y> ksResponse = null;
		try {
			ksResponse = clazz.newInstance();
		} catch (InstantiationException e) {
			log.error(e);
			throw new Ks3ClientException("to instantiate " + clazz
					+ " has occured an exception:(" + e + ")", e);
		} catch (IllegalAccessException e) {
			log.error(e);
			throw new Ks3ClientException("to instantiate " + clazz
					+ " has occured an exception:(" + e + ")", e);
		}
		if (!success(response, ksResponse)) {
			httpRequest.abort();
			throw new Ks3ServiceException(response, StringUtils.join(
					ksResponse.expectedStatus(), ","));
		}
		Y result = ksResponse.handleResponse(response);
		if (httpRequest != null) {
			// 否则链接池会耗尽
			httpRequest.abort();
		}
		log.info("finished handle response : " + Timer.end());
		return result;
	}

	/**
	 * 查看返回的状态码是否为期望的状态码
	 * 
	 * @param response
	 *            {@link HttpResponse}
	 * @param kscResponse
	 *            {@link Ks3WebServiceResponse}
	 * @return 是 ： true 否：false
	 */
	private boolean success(HttpResponse response,
			Ks3WebServiceResponse<?> kscResponse) {
		int num = kscResponse.expectedStatus().length;
		int code = response.getStatusLine().getStatusCode();
		for (int i = 0; i < num; i++) {
			if (code == kscResponse.expectedStatus()[i])
				return true;
		}
		return false;
	}

	/**
	 * 将一次请求的http信息打在日志中
	 * 
	 * @param request
	 * @param response
	 */
	private void doLogHttp(HttpRequestBase request, HttpResponse response) {
		log.info(">>request:");
		log.info(new StringBuffer(">>").append(request.getRequestLine()));
		log.info(">>headers:");
		Header[] headers = request.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			log.info(new StringBuffer(">>").append(headers[i].getName())
					.append(":").append(headers[i].getValue()));
		}
		HttpParams params = request.getParams();
		if (params instanceof BasicHttpParams) {
			log.info(">>params:");
			for (String name : ((BasicHttpParams) params).getNames()) {
				log.info(new StringBuffer(">>").append(name).append(":")
						.append(params.getParameter(name)));
			}
		}
		log.info("<<response");
		log.info(new StringBuffer("<<").append(response.getStatusLine()));
		log.info("<<headers:");
		headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			log.info(new StringBuffer("<<").append(headers[i].getName())
					.append(":").append(headers[i].getValue()));
		}
	}
}
