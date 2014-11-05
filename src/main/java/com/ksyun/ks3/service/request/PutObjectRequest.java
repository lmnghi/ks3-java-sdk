package com.ksyun.ks3.service.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.ksyun.ks3.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.RepeatableInputStream;
import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.ObjectMetadata.Meta;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.http.Mimetypes;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.Md5Utils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 上午10:28:06
 * 
 * @description 
 **/
public class PutObjectRequest extends Ks3WebServiceRequest{
	private File file;
	private ObjectMetadata objectMeta = new ObjectMetadata();
	private CannedAccessControlList cannedAcl;
	private AccessControlList acl = new AccessControlList();
	private String redirectLocation;
	public PutObjectRequest(String bucketname,String key,File file)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(key);
		this.setFile(file);
	}
	public PutObjectRequest(String bucketname,String key,InputStream inputStream,ObjectMetadata metadata)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(key);
		this.setObjectMeta(metadata);
		this.setRequestBody(inputStream);
	}
	
	@Override
	protected void configHttpRequest() {
		this.setContentType("binary/octet-stream");
		/**
		 * 设置request body
		 * meta
		 */
		if(file!=null)
		{
			try {
				this.setRequestBody(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new Ks3ClientException("file :"+file.getName()+" not found");
			}
			objectMeta.setContentType(Mimetypes.getInstance().getMimetype(file));
			long length = file.length();
			objectMeta.setContentLength(String.valueOf(length));
			this.addHeader(HttpHeaders.ContentLength, String.valueOf(length));
			try {
				String contentMd5_b64 = Md5Utils.md5AsBase64(file);
				this.addHeader(HttpHeaders.ContentMD5.toString(), contentMd5_b64);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new Ks3ClientException("file :"+file.getName()+" not found");
			} catch (IOException e) {
				e.printStackTrace();
				throw new Ks3ClientException("calculate file md5 error ("+e+")",e);
			}
		}else
		{
			this.setRequestBody(new MD5DigestCalculatingInputStream(this.getRequestBody()));
		}
		for(Entry<Meta,String> entry:this.objectMeta.getMetadata().entrySet())
		{
			//apache http框架会自动添加content-length
			if(!entry.getKey().equals(Meta.ContentLength))
			{
		    	this.addHeader(entry.getKey().toString(),entry.getValue());
			}
		}
		for(Entry<String,String> entry:this.objectMeta.getUserMetadata().entrySet())
		{
			if(entry.getKey().startsWith(ObjectMetadata.userMetaPrefix))
		    	this.addHeader(entry.getKey(),entry.getValue());
		}
		if(this.cannedAcl!=null)
		{
			this.addHeader(HttpHeaders.CannedAcl.toString(),cannedAcl.toString());
		}
		if(this.acl!=null)
		{
			this.getHeader().putAll(HttpUtils.convertAcl2Headers(acl));
		}
		if(this.redirectLocation!=null)
		{
			this.addHeader(HttpHeaders.XKssWebsiteRedirectLocation, this.redirectLocation);
		}
		this.setHttpMethod(HttpMethod.PUT);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if(file==null&&this.getRequestBody()==null)
			throw new IllegalArgumentException("upload object can not be null");
	}
	public File getFile() {
		return file;
	}
	private void setFile(File file) {
		this.file = file;
	}
	public ObjectMetadata getObjectMeta() {
		return objectMeta;
	}
	public void setObjectMeta(ObjectMetadata objectMeta) {
		this.objectMeta = objectMeta;
	}
	public CannedAccessControlList getCannedAcl() {
		return cannedAcl;
	}
	public void setCannedAcl(CannedAccessControlList cannedAcl) {
		this.cannedAcl = cannedAcl;
	}
	public AccessControlList getAcl() {
		return acl;
	}
	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}
	public String getRedirectLocation() {
		return redirectLocation;
	}
	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}
}
