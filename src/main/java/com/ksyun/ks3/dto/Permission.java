package com.ksyun.ks3.dto;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月22日 上午10:41:31
 * 
 * @description ACL中授权时的权限
 **/
public enum Permission {
	FullControl("FULL_CONTROL", "x-kss-grant-full-control"), 
	Read("READ","x-kss-grant-read"),
	Write("WRITE", "x-kss-grant-write");

	private String permissionString;
	private String headerName;

	private Permission(String permissionString, String headerName) {
		this.permissionString = permissionString;
		this.headerName = headerName;
	}
	public String getHeaderName() {
		return headerName;
	}
	public String toString() {
		return permissionString;
	}
    public static Permission load(String value)
    {
        if(value.equals(FullControl.toString()))
        {
            return Permission.FullControl;
        }
        else if(value.equals(Read.toString()))
        {
            return Permission.Read;
        }
        else if(value.equals(Write.toString()))
        {
            return Permission.Write;
        }
        throw ClientIllegalArgumentExceptionGenerator.notCorrect("permission",value,"FULL_CONTROL,READ,WRITE");
    }
}
