package com.ksyun.ks3.service.request;

import com.ksyun.ks3.http.HttpMethod;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月21日 下午2:15:06
 * 
 * @description Head请求bucket,一般用于查看一个bucket是否在全局中已经存在
 **/
public class HeadBucketRequest extends Ks3WebServiceRequest{
	public HeadBucketRequest(String bucketname)
	{
		super.setBucketname(bucketname);
	}
	public void setBucketname(String bucketname)
	{
		super.setBucketname(bucketname);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.HEAD);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw notNull("bucketname");
	}

}
