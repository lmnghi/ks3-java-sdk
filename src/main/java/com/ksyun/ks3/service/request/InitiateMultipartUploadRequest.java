package com.ksyun.ks3.service.request;

import java.util.ArrayList;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notCorrect;

import java.util.List;
import java.util.Map.Entry;

import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.http.Mimetypes;
import com.ksyun.ks3.utils.DateUtils;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.DateUtils.DATETIME_PROTOCOL;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月23日 上午10:37:41
 * 
 * @description 初始化分块上传
 **/
public class InitiateMultipartUploadRequest extends Ks3WebServiceRequest {
	/**
	 * 设置object的元数据
	 */
	private ObjectMetadata objectMeta = new ObjectMetadata();
	/**
	 * 设置object的acl
	 */
	private AccessControlList acl = new AccessControlList();
	/**
	 * 使用一种快捷的方式设置acl
	 */
	private CannedAccessControlList cannedAcl;
	private String redirectLocation;

	public InitiateMultipartUploadRequest(String bucketname, String objectkey) {
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
	}
	public InitiateMultipartUploadRequest(String bucketname,String objectkey,ObjectMetadata metadata){
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.objectMeta = metadata;
	}

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.POST);
		this.setContentType("binary/octet-stream");
		this.addParams("uploads", null);
		if (this.objectMeta == null)
			this.objectMeta = new ObjectMetadata();
		// 根据object key匹配content-type
		if (StringUtils.isBlank(objectMeta.getContentType()))
			objectMeta.setContentType(Mimetypes.getInstance().getMimetype(
					super.getObjectkey()));
		// 添加meta data content-length 是由Apache HTTP框架自动添加的
		if (!StringUtils.isBlank(this.objectMeta.getContentType()))
			this.addHeader(HttpHeaders.ContentType,
					this.objectMeta.getContentType());
		if (!StringUtils.isBlank(this.objectMeta.getCacheControl()))
			this.addHeader(HttpHeaders.CacheControl,
					this.objectMeta.getCacheControl());
		if (!StringUtils.isBlank(this.objectMeta.getContentDisposition()))
			this.addHeader(HttpHeaders.ContentDisposition,
					this.objectMeta.getContentDisposition());
		if (!StringUtils.isBlank(this.objectMeta.getContentEncoding()))
			this.addHeader(HttpHeaders.ContentEncoding,
					this.objectMeta.getContentEncoding());
		if (this.objectMeta.getHttpExpiresDate() != null)
			this.addHeader(
					HttpHeaders.Expires,
					DateUtils.convertDate2Str(
							this.objectMeta.getHttpExpiresDate(),
							DATETIME_PROTOCOL.RFC1123).toString());
		// 添加user meta
		for (Entry<String, String> entry : this.objectMeta.getAllUserMeta()
				.entrySet()) {
			if (entry.getKey().startsWith(Constants.KS3_USER_META_PREFIX))
				this.addHeader(entry.getKey(), entry.getValue());
		}
		if (this.cannedAcl != null) {
			this.addHeader(HttpHeaders.CannedAcl.toString(),
					cannedAcl.toString());
		}
		if (this.acl != null) {
			this.getHeader().putAll(HttpUtils.convertAcl2Headers(acl));
		}
		if (this.redirectLocation != null) {
			this.addHeader(HttpHeaders.XKssWebsiteRedirectLocation,
					this.redirectLocation);
		}
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.validateBucketName(this.getBucketname()) == null)
			throw notCorrect("bucketname", this.getBucketname(), "详见API文档");
		if (StringUtils.isBlank(this.getObjectkey()))
			throw notNull("objectkey");
		if (this.redirectLocation != null) {
			if (!this.redirectLocation.startsWith("/")
					&& !this.redirectLocation.startsWith("http://")
					&& !this.redirectLocation.startsWith("https://"))
				throw notCorrect("redirectLocation", this.redirectLocation,
						"以 / http:// 或 https://开头");
		}
	}

	public ObjectMetadata getObjectMeta() {
		return objectMeta;
	}

	public void setObjectMeta(ObjectMetadata objectMeta) {
		this.objectMeta = objectMeta;
	}

	public AccessControlList getAcl() {
		return acl;
	}

	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}

	public CannedAccessControlList getCannedAcl() {
		return cannedAcl;
	}

	public void setCannedAcl(CannedAccessControlList cannedAcl) {
		this.cannedAcl = cannedAcl;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}

}
