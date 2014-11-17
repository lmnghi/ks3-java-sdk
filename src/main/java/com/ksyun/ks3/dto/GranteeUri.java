package com.ksyun.ks3.dto;


/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月24日 下午1:18:38
 * 
 * @description 通过uri指定被授权者
 **/
public enum GranteeUri implements Grantee{
	/**
	 * Ks3所有用户
	 */
	AllUsers("http://acs.ksyun.com/groups/global/AllUsers"),
   // AuthenticatedUsers("http://acs.amazonaws.com/groups/global/AuthenticatedUsers"),
   // LogDelivery("http://acs.amazonaws.com/groups/s3/LogDelivery");
	;
	private String uri;
	private GranteeUri(String uri){
		this.uri = uri;
	}
	public String getTypeIdentifier() {
		return "uri";
	}

    public void setIdentifier(String uri) {
        this.uri = uri;
    }

    public String getIdentifier() {
        return uri;
    }
    public static GranteeUri load(String groupUri) {
        for (GranteeUri grantee : GranteeUri.values()) {
            if (grantee.uri.equals(groupUri)) {
                return grantee;
            }
        }
     
        return null;
    }
    @Override
    public String toString()
    {
    	return "GranteeUri[uri="+this.uri+"]";
    }
}
