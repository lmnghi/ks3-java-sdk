package com.ksyun.ks3.service.response;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;

import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.ObjectMetadata.Meta;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.utils.DateUtils;
import com.ksyun.ks3.utils.DateUtils.DATETIME_PROTOCOL;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 下午7:52:42
 * 
 * @description 
 **/
public class HeadObjectResponse extends Ks3WebServiceDefaultResponse<HeadObjectResult>{

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		result = new HeadObjectResult();
		result.setETag(this.getHeader(HttpHeaders.ETag.toString()));
		String time = this.getHeader(HttpHeaders.LastModified.toString());
		if(!StringUtils.isBlank(time))
		    result.setLastmodified(DateUtils.convertStr2Date(this.getHeader(HttpHeaders.LastModified.toString()), DATETIME_PROTOCOL.RFC1123));
		ObjectMetadata meta = new ObjectMetadata();
		Header[] headers = this.getResponse().getAllHeaders();
		for(int i = 0;i<headers.length;i++)
		{
			Header h = headers[i];
			if(h.getName().startsWith(ObjectMetadata.userMetaPrefix))
			{
			    meta.addOrEditUserMeta(h.getName(), h.getValue());
			}
			else if(h.getName().equalsIgnoreCase(Meta.CacheControl.toString()))
			{
				meta.setCacheControl(h.getValue());
			}
			else if(h.getName().equalsIgnoreCase(Meta.ContentDisposition.toString()))
			{
				meta.setContentDisposition(h.getValue());
			}
			else if(h.getName().equalsIgnoreCase(Meta.ContentEncoding.toString()))
			{
				meta.setContentEncoding(h.getValue());
			}
			else if(h.getName().equalsIgnoreCase(Meta.ContentLength.toString()))
			{
				meta.setContentLength(h.getValue());
			}
			else if(h.getName().equalsIgnoreCase(Meta.ContentType.toString()))
			{
				meta.setContentType(h.getValue());
			}
			else if(h.getName().equalsIgnoreCase(Meta.Expires.toString()))
			{
				meta.setExpires(h.getValue());
			}
		}
		result.setObjectMetadata(meta);
	}

}
