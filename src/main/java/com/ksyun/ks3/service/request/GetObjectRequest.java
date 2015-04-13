package com.ksyun.ks3.service.request;

import java.util.ArrayList;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notCorrect;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.ksyun.ks3.dto.ResponseHeaderOverrides;
import com.ksyun.ks3.dto.SSECustomerKey;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.DateUtils;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.DateUtils.DATETIME_PROTOCOL;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月20日 下午7:55:25
 * 
 * @description 获取object
 * <p>支持分块下载，可通过setRange(long,long)实现</p>
 * <p>支持缓存控制，通过matchingETagConstraints、nonmatchingEtagConstraints、unmodifiedSinceConstraint、modifiedSinceConstraint控制</p>
 * <p>支持重写返回的http headers,通过修改overrides实现</p>
 **/
public class GetObjectRequest extends Ks3WebServiceRequest {
	private long [] range = null;
	/**
	 * object的etag能匹配到则返回，否则返回结果的ifPreconditionSuccess为false，object为空
	 */
	private List<String> matchingETagConstraints = new ArrayList<String>();
	/**
	 * object的etag不同于其中的任何一个，否则返回结果的ifModified为false,object为空
	 */
	private List<String> nonmatchingEtagConstraints = new ArrayList<String>();
	/**
	 * 在此时间之后没有被修改过，否则返回结果的ifPreconditionSuccess为false，object为空
	 */
	private Date unmodifiedSinceConstraint;
	/**
	 * 在此时间之后被修改过，否则返回结果的ifModified为false,object为空
	 */
	private Date modifiedSinceConstraint;
	/**
	 * 修改返回的response的headers
	 */
	private ResponseHeaderOverrides overrides = new ResponseHeaderOverrides();
	/**
	 * 指定服务端加密使用的算法及key
	 */
	private SSECustomerKey sseCustomerKey;
	/**
	 * 
	 * @param bucketname
	 * @param key
	 */
	public GetObjectRequest(String bucketname,String key)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(key);
	}
	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.GET);
		if(range!=null&&range.length==2)
			this.addHeader(HttpHeaders.Range,"bytes="+range[0]+"-"+range[1]);
		if(matchingETagConstraints.size()>0)
			this.addHeader(HttpHeaders.IfMatch, StringUtils.join(matchingETagConstraints, ","));
		if(nonmatchingEtagConstraints.size()>0)
			this.addHeader(HttpHeaders.IfNoneMatch, StringUtils.join(nonmatchingEtagConstraints, ","));
		if(this.unmodifiedSinceConstraint !=null)
			this.addHeader(HttpHeaders.IfUnmodifiedSince, DateUtils.convertDate2Str(this.unmodifiedSinceConstraint, DATETIME_PROTOCOL.RFC1123).toString());
		if(this.modifiedSinceConstraint !=null)
			this.addHeader(HttpHeaders.IfModifiedSince, DateUtils.convertDate2Str(this.modifiedSinceConstraint, DATETIME_PROTOCOL.RFC1123).toString());
		this.getParams().putAll(this.getOverrides().getOverrides());
		//添加服务端加密相关
		this.getHeader().putAll(HttpUtils.convertSSECustomerKey2Headers(sseCustomerKey));
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw notNull("bucketname");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw notNull("objectkey");
	}
	public long [] getRange() {
		return range;
	}
	public void setRange(long start,long end) {
		this.range = new long[]{start,end};
	}
	/**
	 * object的etag能匹配到则返回，否则返回结果的ifPreconditionSuccess为false，object为空
	 */
	public List<String> getMatchingETagConstraints() {
		return matchingETagConstraints;
	}
	/**
	 * object的etag能匹配到则返回，否则返回结果的ifPreconditionSuccess为false，object为空
	 */
	public void setMatchingETagConstraints(List<String> matchingETagConstraints) {
		this.matchingETagConstraints = matchingETagConstraints;
	}
	/**
	 * object的etag不同于其中的任何一个，否则返回结果的ifModified为false,object为空
	 */
	public List<String> getNonmatchingEtagConstraints() {
		return nonmatchingEtagConstraints;
	}
	/**
	 * object的etag不同于其中的任何一个，否则返回结果的ifModified为false,object为空
	 */
	public void setNonmatchingEtagConstraints(
			List<String> nonmatchingEtagConstraints) {
		this.nonmatchingEtagConstraints = nonmatchingEtagConstraints;
	}
	/**
	 * 在此时间之后没有被修改过，否则返回结果的ifPreconditionSuccess为false，object为空
	 */
	public Date getUnmodifiedSinceConstraint() {
		return unmodifiedSinceConstraint;
	}
	/**
	 * 在此时间之后没有被修改过，否则返回结果的ifPreconditionSuccess为false，object为空
	 */
	public void setUnmodifiedSinceConstraint(Date unmodifiedSinceConstraint) {
		this.unmodifiedSinceConstraint = unmodifiedSinceConstraint;
	}
	/**
	 * 在此时间之后被修改过，否则返回结果的ifModified为false,object为空
	 */
	public Date getModifiedSinceConstraint() {
		return modifiedSinceConstraint;
	}
	/**
	 * 在此时间之后被修改过，否则返回结果的ifModified为false,object为空
	 */
	public void setModifiedSinceConstraint(Date modifiedSinceConstraint) {
		this.modifiedSinceConstraint = modifiedSinceConstraint;
	}
	public ResponseHeaderOverrides getOverrides() {
		return overrides;
	}
	/**
	 * 修改返回的response的headers
	 */
	public void setOverrides(ResponseHeaderOverrides overrides) {
		this.overrides = overrides;
	}
	public SSECustomerKey getSseCustomerKey() {
		return sseCustomerKey;
	}
	public void setSseCustomerKey(SSECustomerKey sseCustomerKey) {
		this.sseCustomerKey = sseCustomerKey;
	}
	
}
