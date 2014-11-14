package com.ksyun.ks3.dto;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月22日 上午10:40:40
 * 
 * @description 被授权者
 * {@link GranteeEmail}
 * {@link GranteeId}
 * {@link GranteeUri}
 **/
public interface Grantee {
	/**
	 * <p>被授权者类型</p>
	 * <p>Id</p>
	 * <p>Email</p>
	 * <p>Uri</P>
	 * @return
	 */
    public String getTypeIdentifier();

	public void setIdentifier(String id) ;

	public String getIdentifier();
}
