package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;

/**
 * 获取object的acl
 * @author LIJUNWEI
 *
 */
public class GetObjectACLRequest extends Ks3WebServiceRequest{


    @Override
    protected void configHttpRequest() {
        this.setHttpMethod(HttpMethod.GET);
        this.addParams("acl","");
    }

    @Override
    protected void validateParams() throws IllegalArgumentException {
        if(StringUtils.validateBucketName(this.getBucketname())==null)
            throw new IllegalArgumentException("bucket name is not correct");
    }

    public GetObjectACLRequest(String bucketName,String objectName) {
        setBucketname(bucketName);
        setObjectkey(objectName);
    }
}
