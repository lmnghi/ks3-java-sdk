package com.ksyun.ks3.service.request;

import java.io.ByteArrayInputStream;
import java.util.HashSet;

import com.ksyun.ks3.dto.BucketLoggingStatus;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.GranteeEmail;
import com.ksyun.ks3.dto.GranteeId;
import com.ksyun.ks3.dto.GranteeUri;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.XmlWriter;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description 配置bucket日志的请求
 **/
public class PutBucketLoggingRequest extends Ks3WebServiceRequest {
	public PutBucketLoggingRequest(String bucketName, boolean enable,
			String targetBucket) {
		this(bucketName);
		this.bucketLoggingStatus.setEnable(enable);
		this.bucketLoggingStatus.setTargetBucket(targetBucket);
	}

	public PutBucketLoggingRequest(String bucketName, boolean enable,
			String targetBucket, String targetPrefix) {
		this(bucketName);
		this.bucketLoggingStatus.setEnable(enable);
		this.bucketLoggingStatus.setTargetBucket(targetBucket);
		this.bucketLoggingStatus.setTargetPrefix(targetPrefix);
	}

	public PutBucketLoggingRequest(String bucketName,
			BucketLoggingStatus bucketLoggingStatus) {
		this(bucketName);
		this.bucketLoggingStatus = bucketLoggingStatus;
	}

	public PutBucketLoggingRequest(String bucketName) {
		super.setBucketname(bucketName);
	}

	private BucketLoggingStatus bucketLoggingStatus = new BucketLoggingStatus();

	@Override
	protected void configHttpRequest() {
		this.setHttpMethod(HttpMethod.PUT);
		this.addParams("logging", null);

		XmlWriter writer = new XmlWriter();
		writer.startWithNs("BucketLoggingStatus");
		if (this.bucketLoggingStatus.isEnable()) {
			writer.start("LoggingEnabled").start("TargetBucket")
					.value(this.bucketLoggingStatus.getTargetBucket()).end()
					.start("TargetPrefix")
					.value(this.bucketLoggingStatus.getTargetPrefix()).end();
			if (this.bucketLoggingStatus.getTargetGrants() != null
					&& this.bucketLoggingStatus.getTargetGrants().size() != 0) {
				HashSet<Grant> grants = this.bucketLoggingStatus.getTargetGrants();
				for (Grant grant : grants) {
					writer.start("Grant");

					if (grant.getGrantee() instanceof GranteeEmail) {
						writer.start(
								"Grantee",
								new String[] { "xmlns:xsi", "xsi" },
								new String[] {
										"http://www.w3.org/2001/XMLSchema-instance",
										"AmazonCustomerByEmail" });
						writer.start("EmailAddress");
					}else if (grant.getGrantee() instanceof GranteeUri) {
						writer.start(
								"Grantee",
								new String[] { "xmlns:xsi", "xsi" },
								new String[] {
										"http://www.w3.org/2001/XMLSchema-instance",
										"Group" });
						writer.start("URI");
					}else if (grant.getGrantee() instanceof GranteeId) {
						writer.start(
								"Grantee",
								new String[] { "xmlns:xsi", "xsi" },
								new String[] {
										"http://www.w3.org/2001/XMLSchema-instance",
										"CanonicalUser" });
						writer.start("ID");
					}
					writer.value(grant.getGrantee().getIdentifier()).end()
							.end().start("Permission")
							.value(grant.getPermission().toString()).end();

					writer.end();
				}
			}
			writer.end().end();
		} else {
			writer.end();
		}
		this.setRequestBody(new ByteArrayInputStream(writer.toString()
				.getBytes()));
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if (this.bucketLoggingStatus == null)
			throw new IllegalArgumentException(
					"bucketLoggingStatus can not be null");
		if (this.bucketLoggingStatus.isEnable()
				&& this.bucketLoggingStatus.getTargetBucket() == null)
			throw new IllegalArgumentException(
					"targetBucket can not be null when enable is true");
	}

	public BucketLoggingStatus getBucketLoggingStatus() {
		return bucketLoggingStatus;
	}

	public void setBucketLoggingStatus(BucketLoggingStatus bucketLoggingStatus) {
		this.bucketLoggingStatus = bucketLoggingStatus;
	}

}
