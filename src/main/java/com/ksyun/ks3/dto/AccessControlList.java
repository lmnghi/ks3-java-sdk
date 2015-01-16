package com.ksyun.ks3.dto;

import java.util.HashSet;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月22日 上午10:40:23
 * 
 * @description 对bucket或object的权限控制信息
 **/
public class AccessControlList {
	/**
	 * 授权信息
	 */
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
		return StringUtils.object2string(this);
	}
}
