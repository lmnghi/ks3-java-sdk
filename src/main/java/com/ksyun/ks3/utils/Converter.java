package com.ksyun.ks3.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月6日 下午4:00:21
 * 
 * @description 
 **/
public class Converter {
	private static Log log = LogFactory.getLog(Converter.class);
	public static String MD52ETag(String md5)
	{
		String etag = String.format("\"%s\"", Hex.encodeHexString(Base64.decodeBase64(md5)));
		log.info("md5 we calculated is :"+md5+",convert to etag is :"+etag);
		return etag;
	}
}
