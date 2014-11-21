package com.ksyun.ks3.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午3:54:46
 * 
 * @description object元数据
 **/
public class ObjectMetadata {
	/**
	 * 用户自定义的元数据
	 */
	private Map<String, String> userMetadata = new HashMap<String,String>();
	private Map<String, Object> metadata = new HashMap<String,Object>();
	/**
	 * Http Expires,metadat中预留了一个bucket lifecycle的Expires，所以把它放在了外面
	 */
	private Date httpExpiresDate;
	@Override
	public String toString()
	{
		return StringUtils.object2string(this);
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
        return (Date)metadata.get(HttpHeaders.LastModified.toString());
    }
    /**
     * 仅供内部使用
     * @param lastModified
     */
    public void setLastModified(Date lastModified) {
        metadata.put(HttpHeaders.LastModified.toString(), lastModified);
    }
    public long getContentLength() {
        Long contentLength = (Long) metadata.get(HttpHeaders.ContentLength.toString());

        if (contentLength == null) return 0;
        return contentLength;
    }
    /**分块下载时获取文件的总大小*/
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
        return (String)metadata.get(HttpHeaders.ContentType.toString());
    }
    public void setContentType(String contentType) {
        metadata.put(HttpHeaders.ContentType.toString(), contentType);
    }
    public String getContentEncoding() {
        return (String)metadata.get(HttpHeaders.ContentEncoding.toString());
    }
    public void setContentEncoding(String encoding) {
        metadata.put(HttpHeaders.ContentEncoding.toString(), encoding);
    }
    public String getCacheControl() {
        return (String)metadata.get(HttpHeaders.CacheControl.toString());
    }
    public void setCacheControl(String cacheControl) {
        metadata.put(HttpHeaders.CacheControl.toString(), cacheControl);
    }
    public String getContentMD5() {
        return (String)metadata.get(HttpHeaders.ContentMD5.toString());
    }
    public void setContentMD5(String md5Base64) {
        if(md5Base64 == null){
            metadata.remove(HttpHeaders.ContentMD5);
        }else{
            metadata.put(HttpHeaders.ContentMD5.toString(), md5Base64);
        }

    }
    public String getContentDisposition() {
        return (String)metadata.get(HttpHeaders.ContentDisposition.toString());
    }
    public void setContentDisposition(String disposition) {
        metadata.put(HttpHeaders.ContentDisposition.toString(), disposition);
    }
    public String getETag() {
        return (String)metadata.get(HttpHeaders.ETag.toString());
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
