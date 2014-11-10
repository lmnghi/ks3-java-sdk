package com.ksyun.ks3.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.dto.*;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.http.Ks3CoreController;
import com.ksyun.ks3.service.request.AbortMultipartUploadRequest;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.DeleteBucketRequest;
import com.ksyun.ks3.service.request.DeleteMultipleObjectsRequest;
import com.ksyun.ks3.service.request.DeleteObjectRequest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.HeadBucketRequest;
import com.ksyun.ks3.service.request.HeadObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.ListBucketsRequest;
import com.ksyun.ks3.service.request.ListObjectsRequest;
import com.ksyun.ks3.service.request.ListPartsRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.service.response.AbortMultipartUploadResponse;
import com.ksyun.ks3.service.response.CompleteMultipartUploadResponse;
import com.ksyun.ks3.service.response.CreateBucketResponse;
import com.ksyun.ks3.service.response.DeleteBucketResponse;
import com.ksyun.ks3.service.response.DeleteObjectResponse;
import com.ksyun.ks3.service.response.GetObjectResponse;
import com.ksyun.ks3.service.response.HeadBucketResponse;
import com.ksyun.ks3.service.response.HeadObjectResponse;
import com.ksyun.ks3.service.response.InitiateMultipartUploadResponse;
import com.ksyun.ks3.service.response.ListBucketsResponse;
import com.ksyun.ks3.service.response.ListObjectsResponse;
import com.ksyun.ks3.service.response.ListPartsResponse;
import com.ksyun.ks3.service.response.PutObjectResponse;
import com.ksyun.ks3.service.request.*;
import com.ksyun.ks3.service.response.*;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午5:30:30
 * 
 * @description ks3客户端，用户使用时需要先配置{@link ClientConfig},然后初始化一个Ks3Client进行操作
 **/
public class Ks3Client implements Ks3 {
	private Authorization auth;

	public Authorization getAuth() {
		return auth;
	}

	public void setAuth(Authorization auth) {
		this.auth = auth;
	}

	public Ks3Client() {
	}

	public Ks3Client(Authorization auth) {
		this.auth = auth;
	}

	public Ks3Client(String accesskeyid, String accesskeysecret) {
		this.auth = new Authorization(accesskeyid, accesskeysecret);
	}

	private Ks3CoreController client = new Ks3CoreController();

	public List<Bucket> listBuckets() throws Ks3ClientException,
			Ks3ServiceException {
		return listBuckets(new ListBucketsRequest());
	}

	public List<Bucket> listBuckets(ListBucketsRequest request)
			throws Ks3ClientException, Ks3ServiceException {

		return client.execute(auth, request, ListBucketsResponse.class);
	}

	public AccessControlPolicy getBucketACL(String bucketName)
			throws Ks3ClientException, Ks3ServiceException {
		return getBucketACL(new GetBucketACLRequest(bucketName));
	}

	public AccessControlPolicy getBucketACL(GetBucketACLRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		return client.execute(auth, request, GetBucketACLResponse.class);
	}

	public void putBucketACL(String bucketName,
			AccessControlList accessControlList) throws Ks3ClientException,
			Ks3ServiceException {
		putBucketACL(new PutBucketACLRequest(bucketName, accessControlList));
	}

	public void putBucketACL(PutBucketACLRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		client.execute(auth, request, PutBucketACLResponse.class);
	}

	public void putObjectACL(String bucketName, String objectName,
			AccessControlList accessControlList) throws Ks3ClientException,
			Ks3ServiceException {
		putObjectACL(new PutObjectACLRequest(bucketName, objectName,
				accessControlList));
	}

	public void putObjectACL(PutObjectACLRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		client.execute(auth, request, PutObjectACLResponse.class);
	}

	public AccessControlPolicy getObjectACL(String bucketName, String objectName)
			throws Ks3ClientException, Ks3ServiceException {
		return getObjectACL(new GetObjectACLRequest(bucketName, objectName));
	}

	public AccessControlPolicy getObjectACL(GetObjectACLRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		return client.execute(auth, request, GetObjectACLResponse.class);
	}

	public Bucket createBucket(String bucketname) throws Ks3ClientException,
			Ks3ServiceException {
		return createBucket(new CreateBucketRequest(bucketname));
	}

	public Bucket createBucket(CreateBucketRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		Bucket bucket = client.execute(auth, request,
				CreateBucketResponse.class);
		bucket.setName(request.getBucketname());
		return bucket;
	}

	public void deleteBucket(String bucketname) throws Ks3ClientException,
			Ks3ServiceException {
		deleteBucket(new DeleteBucketRequest(bucketname));

	}

	public void deleteBucket(DeleteBucketRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		client.execute(auth, request, DeleteBucketResponse.class);
	}

	public ObjectListing listObjects(String bucketname)
			throws Ks3ClientException, Ks3ServiceException {
		return listObjects(new ListObjectsRequest(bucketname, null, null, null,
				null));
	}

	public ObjectListing listObjects(String bucketname, String prefix)
			throws Ks3ClientException, Ks3ServiceException {
		return listObjects(new ListObjectsRequest(bucketname, prefix, null,
				null, null));
	}

	public ObjectListing listObjects(ListObjectsRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		return client.execute(auth, request, ListObjectsResponse.class);
	}

	public void deleteObject(String bucketname, String key)
			throws Ks3ClientException, Ks3ServiceException {
		deleteObject(new DeleteObjectRequest(bucketname, key));
	}

	public void deleteObject(DeleteObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		client.execute(auth, request, DeleteObjectResponse.class);
	}

	public Ks3Object getObject(String bucketname, String key)
			throws Ks3ClientException, Ks3ServiceException {
		return getObject(new GetObjectRequest(bucketname, key));
	}

	public Ks3Object getObject(GetObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		Ks3Object object = client.execute(auth, request,
				GetObjectResponse.class);
		object.setBucketName(request.getBucketname());
		object.setKey(request.getObjectkey());
		return object;
	}

	public HeadBucketResult headBucket(String bucketname)
			throws Ks3ClientException, Ks3ServiceException {
		return this.headBucket(new HeadBucketRequest(bucketname));
	}

	public HeadBucketResult headBucket(HeadBucketRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		return client.execute(auth, request, HeadBucketResponse.class);
	}

	public boolean bucketExists(String bucketname) throws Ks3ClientException,
			Ks3ServiceException {
		try {
			HeadBucketResult result = this.headBucket(bucketname);
			if (result.getStatueCode() == 404)
				return false;
			if (result.getStatueCode() == 200 || result.getStatueCode() == 301
					|| result.getStatueCode() == 403)
				return true;
			return false;
		} catch (Ks3ClientException e) {
			return false;
		}
	}

	public PutObjectResult PutObject(String bucketname, String objectkey,
			File file) throws Ks3ClientException, Ks3ServiceException {
		return putObject(new PutObjectRequest(bucketname, objectkey, file));
	}

	public PutObjectResult PutObject(String bucketname, String objectkey,
			InputStream inputstream, ObjectMetadata objectmeta)
			throws Ks3ClientException, Ks3ServiceException {
		return putObject(new PutObjectRequest(bucketname, objectkey,
				inputstream, objectmeta));
	}

	public PutObjectResult putObject(PutObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		PutObjectResult obj = client.execute(auth, request,
				PutObjectResponse.class);
		return obj;
	}

	public HeadObjectResult headObject(String bucketname, String objectkey)
			throws Ks3ClientException, Ks3ServiceException {
		return headObject(new HeadObjectRequest(bucketname, objectkey));
	}

	public HeadObjectResult headObject(HeadObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		return client.execute(auth, request, HeadObjectResponse.class);
	}

	public InitiateMultipartUploadResult initiateMultipartUpload(
			String bucketname, String objectkey) throws Ks3ClientException,
			Ks3ServiceException {
		return initiateMultipartUpload(new InitiateMultipartUploadRequest(
				bucketname, objectkey));
	}

	public InitiateMultipartUploadResult initiateMultipartUpload(
			InitiateMultipartUploadRequest request) throws Ks3ClientException,
			Ks3ServiceException {
		InitiateMultipartUploadResult result = client.execute(auth, request,
				InitiateMultipartUploadResponse.class);
		result.setBucket(request.getBucketname());
		result.setKey(request.getObjectkey());
		return result;
	}

	public PartETag uploadPart(UploadPartRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		PartETag result = client.execute(auth, request,
				UploadPartResponse.class);
		result.setPartNumber(request.getPartNumber());
		return result;
	}

	public CompleteMultipartUploadResult completeMultipartUpload(
			String bucketname, String objectkey, String uploadId,
			List<PartETag> partETags) throws Ks3ClientException,
			Ks3ServiceException {
		return completeMultipartUpload(new CompleteMultipartUploadRequest(
				bucketname, objectkey, uploadId, partETags));
	}

	public CompleteMultipartUploadResult completeMultipartUpload(
			CompleteMultipartUploadRequest request) throws Ks3ClientException,
			Ks3ServiceException {
		return client.execute(auth, request,
				CompleteMultipartUploadResponse.class);
	}

	public void abortMultipartUpload(String bucketname, String objectkey,
			String uploadId) throws Ks3ClientException, Ks3ServiceException {
		this.abortMultipartUpload(new AbortMultipartUploadRequest(bucketname,
				objectkey, uploadId));
	}

	public void abortMultipartUpload(AbortMultipartUploadRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		this.client.execute(auth, request, AbortMultipartUploadResponse.class);
	}

	public ListPartsResult ListParts(String bucketname, String objectkey,
			String uploadId) throws Ks3ClientException, Ks3ServiceException {
		ListPartsRequest request = new ListPartsRequest(bucketname, objectkey,
				uploadId);
		return ListParts(request);
	}

	public ListPartsResult ListParts(String bucketname, String objectkey,
			String uploadId, int maxParts) throws Ks3ClientException,
			Ks3ServiceException {
		ListPartsRequest request = new ListPartsRequest(bucketname, objectkey,
				uploadId);
		request.setMaxParts(maxParts);
		return ListParts(request);
	}

	public ListPartsResult ListParts(String bucketname, String objectkey,
			String uploadId, int maxParts, int partNumberMarker)
			throws Ks3ClientException, Ks3ServiceException {
		ListPartsRequest request = new ListPartsRequest(bucketname, objectkey,
				uploadId);
		request.setMaxParts(maxParts);
		request.setPartNumberMarker(partNumberMarker);
		return ListParts(request);
	}

	public ListPartsResult ListParts(ListPartsRequest request)
			throws Ks3ClientException, Ks3ServiceException {
		return client.execute(auth, request, ListPartsResponse.class);
	}

	public void putObjectACL(String bucketName, String objectName,
			CannedAccessControlList accessControlList)
			throws Ks3ClientException, Ks3ServiceException {
		putObjectACL(new PutObjectACLRequest(bucketName, objectName,
				accessControlList));
	}

	public void putBucketACL(String bucketName,
			CannedAccessControlList CannedAcl) throws Ks3ClientException,
			Ks3ServiceException {
		this.putBucketACL(new PutBucketACLRequest(bucketName, CannedAcl));
	}

	public DeleteMultipleObjectsResult deleteObjects(
			DeleteMultipleObjectsRequest request) throws Ks3ClientException,
			Ks3ServiceException {
		return client.execute(auth, request, DeleteMultipleObjectsResponse.class);
	}

	public DeleteMultipleObjectsResult deleteObjects(List<String> keys,String bucketName)
			throws Ks3ClientException, Ks3ServiceException {
		return this.deleteObjects(new DeleteMultipleObjectsRequest(bucketName,keys));
	}

	public DeleteMultipleObjectsResult deleteObjects(String[] keys,String bucketName)
			throws Ks3ClientException, Ks3ServiceException {
		return this.deleteObjects(new DeleteMultipleObjectsRequest(bucketName,keys));
	}

}
