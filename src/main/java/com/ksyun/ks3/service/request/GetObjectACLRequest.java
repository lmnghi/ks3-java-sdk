package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * Created by 杨春建 on 2014/10/20.
 */
public class GetObjectACLRequest extends Ks3WebServiceRequest{


    private String acl;
    @Override
    protected void configHttpRequest() {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("acl",acl);
    }

    @Override
    protected void validateParams() throws IllegalArgumentException {
        if(StringUtils.validateBucketName(this.getBucketname())==null)
            throw new IllegalArgumentException("bucket name is not correct");
    }

    public GetObjectACLRequest() {
    }

    public GetObjectACLRequest(String bucketName,String objectName) {
        setBucketname(bucketName);
        setObjectkey(objectName);
    }
}
