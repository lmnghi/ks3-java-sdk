package com.ksyun.ks3.service.request;

import com.ksyun.ks3.dto.*;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.StringUtils;

/**
 * 修改bucket的acl
 * @author LIJUNWEI
 *
 */
public class PutBucketACLRequest extends Ks3WebServiceRequest{
	/**
	 * bucket的acl
	 */
    private AccessControlList accessControlList;
    /**
     * 一种快捷的配置方式
     */
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
    	if(StringUtils.isBlank(this.getBucketname())){
    		throw notNull("bucketname");
    	}
    	if(this.accessControlList==null&&this.cannedAcl==null)
    		throw notNull("accessControlList","cannedAcl");
    }
    public PutBucketACLRequest(String bucketName)
    {
    	super.setBucketname(bucketName);
    }
    public PutBucketACLRequest(String bucketName,CannedAccessControlList cannedAcl)
    {
    	setBucketname(bucketName);
    	this.setCannedAcl(cannedAcl);
    }
    public PutBucketACLRequest(String bucketName,AccessControlList accessControlList) {
        setBucketname(bucketName);
        this.accessControlList = accessControlList;
    }
}
