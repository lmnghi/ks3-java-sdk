package com.ksyun.ks3.dto;

import java.util.HashSet;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 上午10:40:23
 * 
 * @description 
 **/
public class AccessControlList {
	private HashSet<Grant> grants = new HashSet<Grant>();
	public HashSet<Grant> getGrants() {
		return grants;
	}
	public void setGrants(HashSet<Grant> grants) {
		this.grants = grants;
	}
	public void  addGrant(Grant grant)
	{
		this.grants.add(grant);
	}
	public void addGrant(Grantee grantee,Permission permission)
	{
		Grant grant = new Grant();
		grant.setGrantee(grantee);
		grant.setPermission(permission);
		this.addGrant(grant);
	}
	@Override
	public String toString()
	{
		return grants.toString();
	}
}
