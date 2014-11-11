package com.ksyun.ks3.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;


/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月6日 下午4:00:21
 * 
 * @description 
 **/
public class Converter {
	public static String MD52ETag(String md5)
	{
		return String.format("\"%s\"", Hex.encodeHexString(Base64.decodeBase64(md5)));
	}
}
