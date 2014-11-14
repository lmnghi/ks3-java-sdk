package com.ksyun.ks3.config;

import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[13810414122@163.com]  
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
	public final static String KS3_PACAKAGE = "com.ksyun.ks3";
	public final static String KS3_USER_META_PREFIX = "x-kss-meta-";
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
