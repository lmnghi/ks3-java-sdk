package com.ksyun.ks3.service.request;

import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月15日 下午4:43:12
 * 
 * @description 
 **/
public class CreateBucketRequest extends Ks3WebServiceRequest{
	private CannedAccessControlList cannedAcl;
	private AccessControlList acl = new AccessControlList();
	public CreateBucketRequest(String bucketName)
	{
		this.setBucketname(bucketName);
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

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
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
	}
}
