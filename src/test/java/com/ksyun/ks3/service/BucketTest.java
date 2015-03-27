package com.ksyun.ks3.service;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.serviceside.AccessDeniedException;
import com.ksyun.ks3.exception.serviceside.BucketAlreadyExistsException;
import com.ksyun.ks3.exception.serviceside.BucketNotEmptyException;
import com.ksyun.ks3.exception.serviceside.InvalidLocationConstraintException;
import com.ksyun.ks3.exception.serviceside.NoSuchBucketException;
import com.ksyun.ks3.request.ErrorRegionCreateBucketRequest;
import com.ksyun.ks3.service.response.CreateBucketResponse;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年3月27日 下午5:14:07
 * 
 * @description 
 **/
public class BucketTest extends Ks3ClientTest{
	String bucketName = "bucket-test-sdk-java";
	String bucketName1 = "bucket-test-sdk-java-1";
	@Before
	public void before(){
		if(!client.bucketExists(bucketName)){
			client.createBucket(bucketName);
		}else{
			client.clearBucket(bucketName);
		}
	}
	@After
	public void after(){
		client.putBucketACL(bucketName, CannedAccessControlList.Private);
		client.clearBucket(bucketName);
		client.putBucketLogging(bucketName, false, null);
	}
	@Test(expected=BucketAlreadyExistsException.class)
	public void putBucket(){
		client.createBucket(bucketName);
	}
	@Test(expected=InvalidLocationConstraintException.class)
	public void putBucketErrorRegion(){
		if(client.bucketExists(bucketName1)){
			client.deleteBucket(bucketName1);
		}
		this.controller.execute(auth1,
				new ErrorRegionCreateBucketRequest(bucketName1),
				CreateBucketResponse.class);
	}
	@Test(expected=NoSuchBucketException.class)
	public void deleteBucketNotExists(){
		if(client.bucketExists(bucketName1)){
			client.deleteBucket(bucketName1);
		}
		client.deleteBucket(bucketName1);
	}
	@Test(expected=BucketNotEmptyException.class)
	public void deleteBucketNotEmpty(){
		client.putObject(bucketName, "test", new ByteArrayInputStream("".getBytes()), null);
		client.deleteBucket(bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void deleteOthersBucketPrivate(){
		client1.deleteBucket(bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void deleteOthersBucketPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client1.deleteBucket(bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void deleteOthersBucketPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client1.deleteBucket(bucketName);
	}
	@Test(expected=NoSuchBucketException.class)
	public void getBucketNotExists(){
		if(client.bucketExists(bucketName1)){
			client.deleteBucket(bucketName1);
		}
		client.listObjects(bucketName1);
	}
	@Test
	public void getBucketPrivate(){
		client.listObjects(bucketName);
	}
	@Test
	public void getBucketPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client.listObjects(bucketName);
	}
	@Test
	public void getBucketPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client.listObjects(bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void getBucketOtherPrivate(){
		client1.listObjects(bucketName);
	}
	@Test
	public void getBucketOtherPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client1.listObjects(bucketName);
	}
	@Test
	public void getBucketOtherPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client1.listObjects(bucketName);
	}
	
	@Test
	public void getBucketACLPrivate(){
		client.getBucketACL(bucketName);
	}
	@Test
	public void getBucketACLPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client.getBucketACL(bucketName);
	}
	@Test
	public void getBucketACLPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client.getBucketACL(bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void getBucketACLOtherPrivate(){
		client1.getBucketACL(bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void getBucketACLOtherPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client1.getBucketACL(bucketName);
	}
	//这个被处理成full control了
	@Test
	public void getBucketACLOtherPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client1.getBucketACL(bucketName);
	}
	@Test
	public void getBucketLocation(){
		assertEquals("HANGZHOU",client.getBucketLoaction(bucketName).toString());
	}
	@Test
	public void putBucketLoggingPrivate(){
		client.putBucketACL(bucketName, CannedAccessControlList.Private);
		client.putBucketLogging(bucketName, true, bucketName);
		assertEquals(true,client.getBucketLogging(bucketName).isEnable());
	}
	@Test
	public void putBucketLoggingPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client.putBucketLogging(bucketName, true, bucketName);
		assertEquals(true,client.getBucketLogging(bucketName).isEnable());
	}
	@Test
	public void putBucketLoggingPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client.putBucketLogging(bucketName, true, bucketName);
		assertEquals(true,client.getBucketLogging(bucketName).isEnable());
	}
	
	@Test(expected=AccessDeniedException.class)
	public void putBucketLoggingOtherPrivate(){
		client.putBucketACL(bucketName, CannedAccessControlList.Private);
		client1.putBucketLogging(bucketName, true, bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void putBucketLoggingOtherPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		client1.putBucketLogging(bucketName, true, bucketName);
	}
	@Test(expected=AccessDeniedException.class)
	public void putBucketLoggingOtherPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		client1.putBucketLogging(bucketName, true, bucketName);
	}
	
	@Test
	public void initMultipartAndAbortPrivate(){
		client.putBucketACL(bucketName, CannedAccessControlList.Private);
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(bucketName, "test");
		client.abortMultipartUpload(bucketName, "test", result.getUploadId());
	}
	@Test
	public void initMultipartAndAbortPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(bucketName, "test");
		client.abortMultipartUpload(bucketName, "test", result.getUploadId());
	}
	@Test
	public void initMultipartAndAbortPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(bucketName, "test");
		client.abortMultipartUpload(bucketName, "test", result.getUploadId());
	}
	
	@Test(expected=AccessDeniedException.class)
	public void initMultipartAndAbortOtherPrivate(){
		client.putBucketACL(bucketName, CannedAccessControlList.Private);
		InitiateMultipartUploadResult result = client1.initiateMultipartUpload(bucketName, "test");
		client1.abortMultipartUpload(bucketName, "test", result.getUploadId());
	}
	@Test(expected=AccessDeniedException.class)
	public void initMultipartAndAbortOtherPublicRead(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client1.initiateMultipartUpload(bucketName, "test");
		client1.abortMultipartUpload(bucketName, "test", result.getUploadId());
	}
	@Test
	public void initMultipartAndAbortOtherPublicReadWrite(){
		client.putBucketACL(bucketName, CannedAccessControlList.PublicReadWrite);
		InitiateMultipartUploadResult result = client1.initiateMultipartUpload(bucketName, "test");
		//abort是不允许的
		client.abortMultipartUpload(bucketName, "test", result.getUploadId());
	}
}
