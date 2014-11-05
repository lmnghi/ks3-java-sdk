package com.ksyun.ks3.service.response;

import org.apache.http.Header;

import com.ksyun.ks3.dto.Ks3Object;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.ObjectMetadata.Meta;
import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月20日 下午7:55:52
 * 
 * @description 
 **/
public class GetObjectResponse extends Ks3WebServiceDefaultResponse<Ks3Object> {

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		ObjectMetadata metaData = new ObjectMetadata();
		result = new Ks3Object();
		result.setObjectContent(getContent());
		result.setRedirectLocation(getHeader(HttpHeaders.XKssWebsiteRedirectLocation.toString()));
		Header[] headers = this.getResponse().getAllHeaders();
		for(int i = 0;i<headers.length;i++)
		{
			if(headers[i].getName().startsWith(ObjectMetadata.userMetaPrefix))
			{
				metaData.addOrEditUserMeta(headers[i].getName(), headers[i].getValue());
			}else{
				String name = headers[i].getName();
				if(name.equalsIgnoreCase(ObjectMetadata.Meta.CacheControl.toString()))
				{
					metaData.addOrEditMeta(ObjectMetadata.Meta.CacheControl,headers[i].getValue());
				}else if(name.equalsIgnoreCase(ObjectMetadata.Meta.ContentDisposition.toString()))
				{
					metaData.addOrEditMeta(ObjectMetadata.Meta.ContentDisposition,headers[i].getValue());
				}else if(name.equalsIgnoreCase(ObjectMetadata.Meta.ContentEncoding.toString()))
				{
					metaData.addOrEditMeta(ObjectMetadata.Meta.ContentEncoding,headers[i].getValue());
				}else if(name.equalsIgnoreCase(ObjectMetadata.Meta.ContentLength.toString()))
				{
					metaData.addOrEditMeta(ObjectMetadata.Meta.ContentLength,headers[i].getValue());
				}else if(name.equalsIgnoreCase(ObjectMetadata.Meta.ContentType.toString()))
				{
					metaData.addOrEditMeta(ObjectMetadata.Meta.ContentType,headers[i].getValue());
				}else if(name.equalsIgnoreCase(Meta.Expires.toString()))
				{
					metaData.setExpires(headers[i].getValue());
				}
			}
		}
		result.setObjectMetadata(metaData);
	}

}
