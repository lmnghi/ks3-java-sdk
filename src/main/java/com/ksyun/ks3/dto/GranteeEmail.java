package com.ksyun.ks3.dto;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月24日 下午1:18:12
 * 
 * @description 
 **/
public class GranteeEmail implements Grantee{
	private String email;
	public String getTypeIdentifier() {
		return "emailAddress";
	}
    public void setIdentifier(String email) {
        this.email = email;
    }
    public String getIdentifier() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString()
    {
    	return "GranteeId[email="+this.email+"]";
    }
}
