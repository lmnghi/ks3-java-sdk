package com.ksyun.ks3.dto;

import java.util.HashMap;
import java.util.Map;

import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:54:46
 * 
 * @description
 **/
public class ObjectMetadata {
	public static enum Meta{
		ContentType(HttpHeaders.ContentType),
		CacheControl(HttpHeaders.CacheControl),
		ContentLength(HttpHeaders.ContentLength),
		ContentDisposition(HttpHeaders.ContentDisposition),
		ContentEncoding(HttpHeaders.ContentEncoding),
		Expires(HttpHeaders.Expires)
		;
		private HttpHeaders header;
		public HttpHeaders getHeader()
		{
			return this.header;
		}
		private Meta(HttpHeaders header)
		{
			this.header = header;
		}
		public String toString()
		{
			return header.toString();
		}
	}
	public static final String userMetaPrefix = "x-kss-meta-";
	private Map<String, String> userMetadata = new HashMap<String,String>();
	private Map<Meta, String> metadata = new HashMap<Meta,String>();
	@Override
	public String toString()
	{
		return "ObjectMetadata[metadata="+this.metadata+";userMetadata="+this.userMetadata+"]";
	}
	public Map<Meta, String> getMetadata() {
		return metadata;
	}
	public void setContentType(String s)
	{
		this.metadata.put(Meta.ContentType, s);
	}
	public void setCacheControl(String s)
	{
		this.metadata.put(Meta.CacheControl, s);
	}
	public void setContentDisposition(String s)
	{
		this.metadata.put(Meta.ContentDisposition, s);
	}
	public void setContentEncoding(String s)
	{
		this.metadata.put(Meta.ContentEncoding, s);
	}
	public void setExpires(String s)
	{
		this.metadata.put(Meta.Expires, s);
	}
	public void setContentLength(String s)
	{
		this.metadata.put(Meta.ContentLength, s);
	}
	public void addOrEditUserMeta(String key,String value)
	{
		if(!key.startsWith(ObjectMetadata.userMetaPrefix))
			throw new IllegalArgumentException("key should be start with:"+ObjectMetadata.userMetaPrefix);
		this.userMetadata.put(key, value);
	}
	public void addOrEditMeta(Meta key,String value)
	{
		this.metadata.put(key, value);
	}
	public Map<String, String> getUserMetadata() {
		return userMetadata;
	}
}
