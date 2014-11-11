package com.ksyun.ks3.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:54:46
 * 
 * @description
 **/
public class ObjectMetadata {
	private Map<String, String> userMetadata = new HashMap<String,String>();
	private Map<String, Object> metadata = new HashMap<String,Object>();
	private Date httpExpiresDate;
	@Override
	public String toString()
	{
		return "ObjectMetadata[metadata="+this.metadata+",userMetadata="+this.userMetadata+",httpExpiresDate="+this.httpExpiresDate+"]";
	}
	
	public void setUserMeta(String key,String value)
	{
		this.userMetadata.put(key.startsWith(Constants.KS3_USER_META_PREFIX)?key:Constants.KS3_USER_META_PREFIX+key, value);
	}
	public String getUserMeta(String key)
	{
		return userMetadata.get(key.startsWith(Constants.KS3_USER_META_PREFIX)?key:Constants.KS3_USER_META_PREFIX+key);
	}
	public Map<String, String> getAllUserMeta()
	{
		return this.userMetadata;
	}
	/**仅供内部使用
	 * @param key
	 * @param value
	 */
    public void setHeader(String key, Object value) {
        metadata.put(key, value);
    }
    public Date getLastModified() {
        return (Date)metadata.get(HttpHeaders.LastModified);
    }
    public void setLastModified(Date lastModified) {
        metadata.put(HttpHeaders.LastModified.toString(), lastModified);
    }
    public long getContentLength() {
        Long contentLength = (Long)metadata.get(HttpHeaders.ContentLength);

        if (contentLength == null) return 0;
        return contentLength.longValue();
    }
    public long getInstanceLength() {
        // See Content-Range in
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
        String contentRange = (String)metadata.get(HttpHeaders.ContentRange.toString());
        if (contentRange != null) {
            int pos = contentRange.lastIndexOf("/");
            if (pos >= 0)
                return Long.parseLong(contentRange.substring(pos+1));
        }
        return getContentLength();
    }
    public void setContentLength(long contentLength) {
        metadata.put(HttpHeaders.ContentLength.toString(), contentLength);
    }
    public String getContentType() {
        return (String)metadata.get(HttpHeaders.ContentType);
    }
    public void setContentType(String contentType) {
        metadata.put(HttpHeaders.ContentType.toString(), contentType);
    }
    public String getContentEncoding() {
        return (String)metadata.get(HttpHeaders.ContentEncoding);
    }
    public void setContentEncoding(String encoding) {
        metadata.put(HttpHeaders.ContentEncoding.toString(), encoding);
    }
    public String getCacheControl() {
        return (String)metadata.get(HttpHeaders.CacheControl);
    }
    public void setCacheControl(String cacheControl) {
        metadata.put(HttpHeaders.CacheControl.toString(), cacheControl);
    }
    public String getContentMD5() {
        return (String)metadata.get(HttpHeaders.ContentMD5);
    }
    public void setContentMD5(String md5Base64) {
        if(md5Base64 == null){
            metadata.remove(HttpHeaders.ContentMD5);
        }else{
            metadata.put(HttpHeaders.ContentMD5.toString(), md5Base64);
        }

    }
    public String getContentDisposition() {
        return (String)metadata.get(HttpHeaders.ContentDisposition);
    }
    public void setContentDisposition(String disposition) {
        metadata.put(HttpHeaders.ContentDisposition.toString(), disposition);
    }
    public String getETag() {
        return (String)metadata.get(HttpHeaders.ETag);
    }
    public void setHttpExpiresDate(Date httpExpiresDate) {
        this.httpExpiresDate = httpExpiresDate;
    }
    public Date getHttpExpiresDate() {
        return httpExpiresDate;
    }
    public Object getMeta(String key)
    {
    	return this.metadata.get(key);
    }
}
