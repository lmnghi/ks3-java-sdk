package com.ksyun.ks3.http;

import java.io.IOException;
import java.io.InputStream;

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
import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.exception.client.InvalidDigestException;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.service.request.support.MD5CalculateAble;
import com.ksyun.ks3.service.response.Ks3WebServiceResponse;
import com.ksyun.ks3.service.response.support.Md5CheckAble;
import com.ksyun.ks3.signer.Signer;
import com.ksyun.ks3.utils.AuthUtils;
import com.ksyun.ks3.utils.Converter;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.Timer;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月14日 下午6:59:38
 * 
 * @description ks3 sdk Ks3WebServiceRequest执行，K3WebServiceResponse解析核心控制器
 **/
public class Ks3CoreController {
	private static final Log log = LogFactory.getLog(Ks3CoreController.class);
	private HttpClientFactory factory = new HttpClientFactory();
	private HttpClient client;

	public <X extends Ks3WebServiceResponse<Y>, Y> Y execute(
			Authorization auth, Ks3WebServiceRequest request, Class<X> clazz) {
		log.info("Ks3WebServiceRequest:"+request.getClass()+";Ks3WebServiceResponse:"+clazz);
		Y result = null;
		try {
			if (auth == null || StringUtils.isBlank(auth.getAccessKeyId())
					|| StringUtils.isBlank(auth.getAccessKeySecret()))
				throw new Ks3ClientException(
						"client not login!AccessKeyId or AccessKeySecret is blank");
			if (request == null || clazz == null)
				throw new IllegalArgumentException();
			result = doExecute(auth, request, clazz);
			return result;
		}
		catch (RuntimeException e) {
			if(e instanceof Ks3ClientException){
				
			}else{
				e = new Ks3ClientException(e);
			}
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (Exception e){
			Ks3ClientException exception = new Ks3ClientException(e);
			log.error(exception);
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			System.out.println();
		}
	}

	private <X extends Ks3WebServiceResponse<Y>, Y> Y doExecute(
			Authorization auth, Ks3WebServiceRequest request, Class<X> clazz) {
		Timer.start();
		this.client = this.factory.createHttpClient();
		HttpResponse response = null;
		HttpRequestBase httpRequest = request.getHttpRequest();
		log.info("finished convert httprequest : " + Timer.end());
		try {
			String signerString = ClientConfig.getConfig().getStr(
					ClientConfig.CLIENT_SIGNER);
			Signer signer = (Signer) Class.forName(signerString).newInstance();
			httpRequest.addHeader(HttpHeaders.Authorization.toString(),
					signer.calculate(auth, request));
		} catch (Exception e) {
			throw new Ks3ClientException(
					"calculate user authorization has occured an exception ("
							+ e + ")", e);
		}
		log.info("finished calculate authorization: " + Timer.end());
		try {
			log.info("sending http request..... please wait");
			log.info(httpRequest.getRequestLine());
			response = client.execute(httpRequest);
			log.info(response.getStatusLine());
			//TODO 307retry
			log.info("finished send request to ks3 service and recive response from the service : "
					+ Timer.end());
		} catch (Exception e) {
			throw new Ks3ClientException(
					"Request to Ks3 has occured an exception:(" + e + ")", e);
		}
		Ks3WebServiceResponse<Y> ksResponse = null;
		try {
			ksResponse = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new Ks3ClientException("to instantiate " + clazz
					+ " has occured an exception:(" + e + ")", e);
		} catch (IllegalAccessException e) {
			throw new Ks3ClientException("to instantiate " + clazz
					+ " has occured an exception:(" + e + ")", e);
		}
		if (!success(response, ksResponse)) {
			httpRequest.abort();
			throw new Ks3ServiceException(response, StringUtils.join(
					ksResponse.expectedStatus(), ",")
					+ "("
					+ Ks3WebServiceResponse.allStatueCode
					+ " is all statue codes)").convert();
		}
		Y result = ksResponse.handleResponse(httpRequest,response);
		if (ksResponse instanceof Md5CheckAble
				&& request instanceof MD5CalculateAble) {
			String ETag = ((Md5CheckAble) ksResponse).getETag();
			String MD5 = ((MD5CalculateAble) request).getMd5();
			log.info("returned etag is:"+ETag);
			if (!ETag.equals(Converter.MD52ETag(MD5))) {
				throw new InvalidDigestException(
						"success,but the MD5 value we calculated dose not match the MD5 value Ks3 Service returned,the part of data may has been lost");
			}
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
			if (kscResponse.expectedStatus()[i] == Ks3WebServiceResponse.allStatueCode)
				return true;
			if (code == kscResponse.expectedStatus()[i])
				return true;
		}
		return false;
	}
}
