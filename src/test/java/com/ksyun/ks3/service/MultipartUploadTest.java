package com.ksyun.ks3.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.ksyun.ks3.UploadPartTime;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.exception.serviceside.AccessDeniedException;
import com.ksyun.ks3.exception.serviceside.NoSuchBucketException;
import com.ksyun.ks3.exception.serviceside.NoSuchUploadException;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.ListPartsRequest;
import com.ksyun.ks3.service.request.PutBucketACLRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.utils.Timer;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月19日 上午11:10:57
 * 
 * @description
 **/
public class MultipartUploadTest extends Ks3ClientTest {
	String bucketName = "lijunwei.sdk.test.multi";
	String file = "file.rar";

	@Test
	public void testInitAndAbort_1031() throws Exception {
		if (client1.bucketExists(bucketName)) {
			client1.clearBucket(bucketName);
			client1.deleteBucket(bucketName);
		}
		try {
			this.isc = false;
			InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
					bucketName, file);
			try {
				client1.initiateMultipartUpload(request);
			} catch (NoSuchBucketException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.createBucket(bucketName);

			PutBucketACLRequest aclRequest = new PutBucketACLRequest(bucketName);

			aclRequest.setCannedAcl(CannedAccessControlList.Private);
			client1.putBucketACL(aclRequest);
			InitiateMultipartUploadResult result1 = client1
					.initiateMultipartUpload(request);
			client1.abortMultipartUpload(result1.getBucket(), result1.getKey(),
					result1.getUploadId());

			aclRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(aclRequest);
			InitiateMultipartUploadResult result2 = client1
					.initiateMultipartUpload(request);
			client1.abortMultipartUpload(result2.getBucket(), result2.getKey(),
					result2.getUploadId());

			aclRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(aclRequest);
			InitiateMultipartUploadResult result3 = client1
					.initiateMultipartUpload(request);
			client1.abortMultipartUpload(result3.getBucket(), result3.getKey(),
					result3.getUploadId());

			aclRequest.setCannedAcl(CannedAccessControlList.Private);
			client1.putBucketACL(aclRequest);
			this.isc = false;
			try {
				InitiateMultipartUploadResult result4 = client2
						.initiateMultipartUpload(request);
				client2.abortMultipartUpload(result4.getBucket(),
						result4.getKey(), result4.getUploadId());
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			aclRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(aclRequest);
			this.isc = false;
			try {
				InitiateMultipartUploadResult result5 = client2
						.initiateMultipartUpload(request);
				client2.abortMultipartUpload(result5.getBucket(),
						result5.getKey(), result5.getUploadId());
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			aclRequest.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(aclRequest);
			InitiateMultipartUploadResult result6 = client2
					.initiateMultipartUpload(request);
			this.isc = false;
			try {
				client2.abortMultipartUpload(result6.getBucket(),
						result6.getKey(), result6.getUploadId());
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

		} finally {
			if (client1.bucketExists(bucketName)) {
				client1.clearBucket(bucketName);
				client1.deleteBucket(bucketName);
			}
		}
	}

	@Test
	public void testInitAndComplete_1032() {
		if (client1.bucketExists(bucketName)) {
			client1.clearBucket(bucketName);
			client1.deleteBucket(bucketName);
		}
		try {
			client1.createBucket(bucketName);

			InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
					bucketName, file);
			InitiateMultipartUploadResult result1 = client1
					.initiateMultipartUpload(request);
			this.isc = false;
			try {
				this.client1.completeMultipartUpload(result1.getBucket(),
						result1.getKey(), result1.getUploadId() + "1", null);
			} catch (NoSuchUploadException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.client1.completeMultipartUpload(result1.getBucket(),
					result1.getKey(), result1.getUploadId(), null);
		} finally {
			if (client1.bucketExists(bucketName)) {
				client1.clearBucket(bucketName);
				client1.deleteBucket(bucketName);
			}
		}
	}

	@Test
	public void testInitAndComplete_1033() {
		if (client1.bucketExists(bucketName)) {
			client1.clearBucket(bucketName);
			client1.deleteBucket(bucketName);
		}
		try {
			client1.createBucket(bucketName);

			long part = 5 * 1024 * 1024;
			String bucket = bucketName;
			URL filename = this.getClass().getClassLoader()
					.getResource("git.exe");
			InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
					bucket, file);
			request1.setCannedAcl(CannedAccessControlList.PublicRead);
			InitiateMultipartUploadResult result = client1
					.initiateMultipartUpload(request1);
			System.out.println(result);
			// upload
			File file = new File(filename.toString().substring(6));
			long n = file.length() / part;
			for (int i = 0; i <= n; i++) {
				UploadPartRequest request = new UploadPartRequest(
						result.getBucket(), result.getKey(),
						result.getUploadId(), i + 1, file, part, (long) i
								* part);
				PartETag tag = client1.uploadPart(request);
				System.out.println(String.valueOf(i + 1) + "  " + tag + "\n");
				try {
					UploadPartTime.print(i + 1, Timer.end());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// list parts
			ListPartsRequest requestList = new ListPartsRequest(
					result.getBucket(), result.getKey(), result.getUploadId());
			ListPartsResult tags = client1.listParts(requestList);
			CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
					tags);
			// complete
			int i = 0;
			for (PartETag etag : request.getPartETags()) {
				if (etag.getPartNumber() == 1) {
					request.getPartETags().remove(i);
					break;
				}
				i++;
			}
			this.isc = false;
			try {
				client1.completeMultipartUpload(request);
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			i = 0;
			for (PartETag etag : request.getPartETags()) {
				if (etag.getPartNumber() == 2) {
					request.getPartETags().remove(i);
					break;
				}
				i++;
			}
			this.isc = false;
			try {
				client1.completeMultipartUpload(request);
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			request.getPartETags().remove(n - 1);
			client1.completeMultipartUpload(request);

		} finally {
			if (client1.bucketExists(bucketName)) {
				client1.clearBucket(bucketName);
				client1.deleteBucket(bucketName);
			}
		}
	}

	@Test
	public void testInitAndComplete_1034() {
		if (client1.bucketExists(bucketName)) {
			client1.clearBucket(bucketName);
			client1.deleteBucket(bucketName);
		}
		try {
			client1.createBucket(bucketName);

			long part = 5 * 1024 * 1024 - 1;
			String bucket = bucketName;
			URL filename = this.getClass().getClassLoader()
					.getResource("git.exe");
			InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
					bucket, file);
			request1.setCannedAcl(CannedAccessControlList.PublicRead);
			InitiateMultipartUploadResult result = client1
					.initiateMultipartUpload(request1);
			System.out.println(result);
			// upload
			File file = new File(filename.toString().substring(6));
			long n = file.length() / part;
			for (int i = 0; i <= n; i++) {
				UploadPartRequest request = new UploadPartRequest(
						result.getBucket(), result.getKey(),
						result.getUploadId(), i + 1, file, part, (long) i
								* part);
				PartETag tag = null;
				this.isc = false;
				try {
					 tag = client1.uploadPart(request);
				} catch (Ks3ClientException e) {
					this.isc = true;
				}
				if (!isc)
					throw new NotThrowException();
				System.out.println(String.valueOf(i + 1) + "  " + tag + "\n");
				try {
					UploadPartTime.print(i + 1, Timer.end());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// list parts
			ListPartsRequest requestList = new ListPartsRequest(
					result.getBucket(), result.getKey(), result.getUploadId());
			ListPartsResult tags = client1.listParts(requestList);
			CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
					tags);
			// complete
			this.isc = false;
			try {
				client1.completeMultipartUpload(request);
			} catch (Ks3ClientException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

		} finally {
			if (client1.bucketExists(bucketName)) {
				client1.clearBucket(bucketName);
				client1.deleteBucket(bucketName);
			}
		}
	}
}
