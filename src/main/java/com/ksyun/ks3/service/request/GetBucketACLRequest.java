package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

import java.security.Permission;

/**
 * 获取bucket acl的请求
 * @author LIJUNWEI
 *
 */
public class GetBucketACLRequest extends Ks3WebServiceRequest{


    @Override
    protected void configHttpRequest() {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("acl","");
    }

    @Override
    protected void validateParams() throws IllegalArgumentException {
        if(StringUtils.isBlank(this.getBucketname()))
            throw new IllegalArgumentException("bucket name is not correct");
    }

    public GetBucketACLRequest(String bucketName) {
        setBucketname(bucketName);
    }
}
