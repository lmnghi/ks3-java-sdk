package com.ksyun.ks3.service.request;

import com.ksyun.ks3.dto.*;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.XmlWriter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by 杨春建 on 2014/10/20.
 */
public class PutObjectACLRequest extends Ks3WebServiceRequest{
    private AccessControlList accessControlList;
    private CannedAccessControlList cannedAcl;

    public CannedAccessControlList getCannedAcl() {
        return cannedAcl;
    }

    public void setCannedAcl(CannedAccessControlList cannedAcl) {
        this.cannedAcl = cannedAcl;
    }
  
    public AccessControlList getAccessControlList() {
		return accessControlList;
	}

	public void setAccessControlList(AccessControlList accessControlList) {
		this.accessControlList = accessControlList;
	}

    @Override
    protected void configHttpRequest() {

        this.setHttpMethod(HttpMethod.PUT);
        this.addParams("acl", "");
        if(getCannedAcl()!=null){
            this.addHeader(HttpHeaders.CannedAcl,getCannedAcl().toString());
        }

        if(this.accessControlList!=null)
        {
            this.getHeader().putAll(HttpUtils.convertAcl2Headers(accessControlList));
        }
    }

    @Override
    protected void validateParams() throws IllegalArgumentException {

    }
    public PutObjectACLRequest(String bucketName,String objectName) {
        setBucketname(bucketName);
        setObjectkey(objectName);
    }
    public PutObjectACLRequest(String bucketName,String objectName,AccessControlList accessControlList){
    	setBucketname(bucketName);
        setObjectkey(objectName);
        this.setAccessControlList(accessControlList);
    }
    public PutObjectACLRequest(String bucketName,String objectName,CannedAccessControlList cannedAcl){
    	setBucketname(bucketName);
        setObjectkey(objectName);
        this.setCannedAcl(cannedAcl);
    }
    public PutObjectACLRequest(String bucketName,String objectName,AccessControlList accessControlList,CannedAccessControlList cannedAcl){
    	setBucketname(bucketName);
        setObjectkey(objectName);
        this.setAccessControlList(accessControlList);
        this.setCannedAcl(cannedAcl);
    }
}
