package com.ksyun.ks3.config;

import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月14日 下午6:19:46
 * 
 * @description 系统常量
 **/
public class Constants {
	/**
	 * xml namespace
	 */
	public final static String KS3_XML_NAMESPACE = "http://s3.amazonaws.com/doc/2006-03-01/";
	public final static String KS3_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	
	
	public final static String KS3_PACAKAGE = "com.ksyun.ks3";
	public final static String KS3_USER_META_PREFIX = "x-kss-meta-";
	
	public final static String KS3_SDK_USER_AGENT = "ks3-kss-java-sdk";
	public final static String KS3_CDN_END_POINT = "kssws.ks-cdn.com";
	public static final int DEFAULT_STREAM_BUFFER_SIZE = 128 * 1024;
	public static final int minPartNumber = 1;
	public static final int maxPartNumber = 10000;
	public static final int minPartSize = 5*1024*1024;
	public static final int maxPartSize = 5*1024*1024*1024;
	public static final long maxSingleUpload = 5*1024*1024*1024;
	/**
	 * object meta 要忽略的headers
	 */
	public final static List<String> KS3_IGNOREG_HEADERS = new ArrayList<String>();
	static{
		KS3_IGNOREG_HEADERS.add(HttpHeaders.Date.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.Server.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.RequestId.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.ExtendedRequestId.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.XApplicationContext.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.XBlackList.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.XWhiteList.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.XNoReferer.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.AcceptRanges.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.Connection.toString());
		KS3_IGNOREG_HEADERS.add(HttpHeaders.XKssOp.toString());
	}
}
