package com.ksyun.ks3.request;

import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.PutBucketACLRequest;
import com.ksyun.ks3.utils.HttpUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月18日 下午1:54:04
 * 
 * @description 
 **/
public class ErrorCannedAclPutBucketAclRequest extends PutBucketACLRequest{
	String acl = "not-support";
	public ErrorCannedAclPutBucketAclRequest(String bucketName) {
		super(bucketName);
	}
	@Override
    protected void configHttpRequest() {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("acl", "");
        this.addHeader(HttpHeaders.CannedAcl,acl);
    }
	@Override
	protected void validateParams(){
		
	}
}
