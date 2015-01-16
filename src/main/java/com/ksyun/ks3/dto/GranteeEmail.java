package com.ksyun.ks3.dto;

import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月24日 下午1:18:12
 * 
 * @description 通过email指定被授权者
 **/
public class GranteeEmail implements Grantee{
	private String email;
	public GranteeEmail(){
		
	}
	public GranteeEmail(String email){
		this.setIdentifier(email);
	}
	public String getTypeIdentifier() {
		return "emailAddress";
	}
    public void setIdentifier(String email) {
        this.email = email;
    }
    public String getIdentifier() {
        return email;
    }

    @Override
    public String toString()
    {
    	return StringUtils.object2string(this);
    }
}
