package com.ksyun.ks3.service.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.dto.ObjectMetadata.Meta;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 上午10:37:41
 * 
 * @description 
 **/
public class InitiateMultipartUploadRequest extends Ks3WebServiceRequest{
	private ObjectMetadata objectMeta = new ObjectMetadata();
	private AccessControlList acl = new AccessControlList();
	private CannedAccessControlList cannedAcl;
	public InitiateMultipartUploadRequest(String bucketname,String objectkey)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.POST);
		this.addParams("uploads", null);
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
			List<String> grants_fullcontrol= new ArrayList<String>();
			List<String> grants_read= new ArrayList<String>();
			List<String> grants_write= new ArrayList<String>();
			for(Grant grant:acl.getGrants())
			{
				if(grant.getPermission().equals(Permission.FullControl))
				{
					grants_fullcontrol.add("id=\""+grant.getGrantee().getIdentifier()+"\"");
				}
				else if(grant.getPermission().equals(Permission.Read))
				{
					grants_read.add("id=\""+grant.getGrantee().getIdentifier()+"\"");
				}
				else if(grant.getPermission().equals(Permission.Write))
				{
					grants_write.add("id=\""+grant.getGrantee().getIdentifier()+"\"");
				}
			}
			if(grants_fullcontrol.size()>0)
			{
				this.addHeader(HttpHeaders.GrantFullControl,StringUtils.join(grants_fullcontrol,","));
			}
			if(grants_read.size()>0)
			{
				this.addHeader(HttpHeaders.GrantRead,StringUtils.join(grants_read,","));
			}
			if(grants_write.size()>0)
			{
				this.addHeader(HttpHeaders.GrantWrite,StringUtils.join(grants_write,","));
			}
		}
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.validateBucketName(this.getBucketname())==null)
			throw new IllegalArgumentException("bucket name is not correct");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
	}
	
	public ObjectMetadata getObjectMeta() {
		return objectMeta;
	}
	public void setObjectMeta(ObjectMetadata objectMeta) {
		this.objectMeta = objectMeta;
	}
	public AccessControlList getAcl() {
		return acl;
	}
	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}
	public CannedAccessControlList getCannedAcl() {
		return cannedAcl;
	}
	public void setCannedAcl(CannedAccessControlList cannedAcl) {
		this.cannedAcl = cannedAcl;
	}
	
}
