package com.ksyun.ks3.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ksyun.ks3.utils.StringUtils;

/**
 * Created by 杨春建 on 2014/10/20.
 */
public class AccessControlPolicy {

	private AccessControlList accessControlList = new AccessControlList();
    private Owner owner;

    public Owner getOwner() {
        return owner;
    }
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
	public AccessControlList getAccessControlList() {
		return accessControlList;
	}
	public void setAccessControlList(AccessControlList accessControlList) {
		this.accessControlList = accessControlList;
	}
    public Set<Grant> getGrants()
    {
    	return this.accessControlList.getGrants();
    }
    public void setGrants(HashSet<Grant> grants)
    {
    	this.accessControlList.setGrants(grants);
    }
    public String toString()
    {
    	return StringUtils.object2string(this);
    }
}
