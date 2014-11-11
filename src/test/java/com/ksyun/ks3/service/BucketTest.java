package com.ksyun.ks3.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.exception.serviceside.AccessDeniedException;
import com.ksyun.ks3.exception.serviceside.BucketAlreadyExistsException;
import com.ksyun.ks3.exception.serviceside.BucketNotEmptyException;
import com.ksyun.ks3.exception.serviceside.InvalidLocationConstraintException;
import com.ksyun.ks3.exception.serviceside.NoSuchBucketException;
import com.ksyun.ks3.exception.serviceside.TooManyBucketsException;
import com.ksyun.ks3.request.ErrorCannedAclCreateBucketRequest;
import com.ksyun.ks3.request.ErrorRegionCreateBucketRequest;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.ListObjectsRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.response.CreateBucketResponse;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月11日 上午10:55:29
 * 
 * @description
 **/
public class BucketTest extends Ks3ClientTest {
	private String bucket = "test.ljw.sdk.001";

	@Test
	public void testPutBucket_1001() {
		try {
			if (client1.bucketExists(bucket))
				client1.deleteBucket(bucket);
			if (client2.bucketExists(bucket))
				client2.deleteBucket(bucket);

			client1.createBucket(bucket);
			ste = true;
			isc = false;
			try {
				client1.createBucket(bucket);
			} catch (BucketAlreadyExistsException e) {
				isc = true;
			}
			if (!isc) {
				throw new NotThrowException();
			}

			ste = true;
			isc = false;
			try {
				client2.createBucket(bucket);
			} catch (BucketAlreadyExistsException e) {
				isc = true;
			}
			if (!isc) {
				throw new NotThrowException();
			}
		} finally {
			if (client1.bucketExists(bucket))
				client1.deleteBucket(bucket);
			if (client2.bucketExists(bucket))
				client2.deleteBucket(bucket);

		}
	}

	@Test
	public void testCreateBucket_1002() {
		List<String> skiped = new ArrayList<String>();
		String[] bucketNames = new String[] {
				"ap",
				"qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm",
				"AAAAAAA", "lijunwe.te st.sdk", "lijunwe.te\tst.sdk",
				"lijunwe.te\rst.sdk", "lijunwe.te\nst.sdk", "dddd...defe",
				"aaa._a", "aaaaa$$", "dddd|||", "2", "ffff.", "eferes-" };
		for (int i = 0; i < bucketNames.length; i++) {
			ste = true;
			isc = false;
			try {
				this.client1.createBucket(bucketNames[i]);
			} catch (Ks3ClientException e) {
				isc = true;
			}
			if (isc == false) {
				skiped.add(bucketNames[i]);
			}
		}
		for (String s : skiped)
			client1.deleteBucket(s);
		System.out.println(skiped);
		if (skiped.size() != 0)
			throw new NotThrowException();
	}

	@Test
	public void testCreateBucket_1003() {
		try {
			if (client1.bucketExists(bucket))
				client1.deleteBucket(bucket);
			this.ste = true;
			this.isc = false;
			try {
				this.controller.execute(auth1,
						new ErrorRegionCreateBucketRequest(bucket),
						CreateBucketResponse.class);
			} catch (InvalidLocationConstraintException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
		} finally {
			if (client1.bucketExists(bucket))
				client1.deleteBucket(bucket);
		}
	}

	@Test
	public void testCreateBucket_1004() {
		try {
			if (client1.bucketExists(bucket))
				client1.deleteBucket(bucket);
			this.ste = true;
			this.isc = false;
			try {
				this.controller
						.execute(auth1, new ErrorCannedAclCreateBucketRequest(
								bucket, "PublicR"), CreateBucketResponse.class);
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
		} finally {
			if (client1.bucketExists(bucket))
				client1.deleteBucket(bucket);
		}
	}

	@Test
	public void testDeleteBucket_1005() {
		if (client1.bucketExists(bucket))
			client1.deleteBucket(bucket);
		this.ste = true;
		this.isc = false;
		try {
			client1.deleteBucket(bucket);
		} catch (NoSuchBucketException e) {
			this.isc = true;
		}
		if (!isc)
			throw new NotThrowException();
	}

	@Test
	public void testDeleteBucket_1006() {
		try {
			if (client1.bucketExists(bucket)) {
				client1.deleteBucket(bucket);
			}
			client1.createBucket(bucket);
			client1.PutObject(bucket, "dir/", new ByteArrayInputStream(
					new byte[] {}), null);
			this.ste = true;
			this.isc = false;
			try {
				client1.deleteBucket(bucket);
			} catch (BucketNotEmptyException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
		} finally {
			if (client1.bucketExists(bucket)) {
				client1.deleteObject(bucket, "dir/");
				client1.deleteBucket(bucket);
			}
		}

	}

	@Test
	public void testDeleteBucket_1007() {
		String bucket1 = bucket + ".001";
		String bucket2 = bucket + ".002";
		String bucket3 = bucket + ".003";
		CreateBucketRequest request1 = new CreateBucketRequest(bucket1,
				CannedAccessControlList.Private);
		CreateBucketRequest request2 = new CreateBucketRequest(bucket2,
				CannedAccessControlList.PublicRead);
		CreateBucketRequest request3 = new CreateBucketRequest(bucket3,
				CannedAccessControlList.PublicReadWrite);
		if (client1.bucketExists(bucket1))
			client1.deleteBucket(bucket1);
		if (client1.bucketExists(bucket2))
			client1.deleteBucket(bucket2);
		if (client1.bucketExists(bucket3))
			client1.deleteBucket(bucket3);
		client1.createBucket(request1);
		client1.createBucket(request2);
		client1.createBucket(request3);
		client1.deleteBucket(bucket1);
		client1.deleteBucket(bucket2);
		client1.deleteBucket(bucket3);
	}

	@Test
	public void testDeleteBucket_1008() {
		String bucket1 = bucket + ".001";
		String bucket2 = bucket + ".002";
		String bucket3 = bucket + ".003";
		CreateBucketRequest request1 = new CreateBucketRequest(bucket1,
				CannedAccessControlList.Private);
		CreateBucketRequest request2 = new CreateBucketRequest(bucket2,
				CannedAccessControlList.PublicRead);
		CreateBucketRequest request3 = new CreateBucketRequest(bucket3,
				CannedAccessControlList.PublicReadWrite);
		if (client2.bucketExists(bucket1))
			client2.deleteBucket(bucket1);
		if (client2.bucketExists(bucket2))
			client2.deleteBucket(bucket2);
		if (client2.bucketExists(bucket3))
			client2.deleteBucket(bucket3);
		client2.createBucket(request1);
		client2.createBucket(request2);
		client2.createBucket(request3);
		try {
			this.ste = true;
			this.isc = false;
			try {
				client1.deleteBucket(bucket1);
			} catch (AccessDeniedException e) {
				isc = true;
			}

			if (!isc)
				throw new NotThrowException();
			this.ste = true;
			this.isc = false;
			try {
				client1.deleteBucket(bucket2);
			} catch (AccessDeniedException e) {
				isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.ste = true;
			this.isc = false;
			try {
				client1.deleteBucket(bucket3);
			} catch (AccessDeniedException e) {
				isc = true;
			}
			if (!isc)
				throw new NotThrowException();
		} finally {
			if (client2.bucketExists(bucket1))
				client2.deleteBucket(bucket1);
			if (client2.bucketExists(bucket2))
				client2.deleteBucket(bucket2);
			if (client2.bucketExists(bucket3))
				client2.deleteBucket(bucket3);
		}
	}

	@Test
	public void testPutBucket_1009() {
		String thisBucket = "";
		List<String> added = new ArrayList<String>();
		List<Bucket> buckets = client1.listBuckets();
		try {
			this.ste = true;
			this.isc = false;
			try {
				for (int i = buckets.size(); i < 21; i++) {
					thisBucket = bucket + ".00" + i + ".dds";
					if (!this.client1.bucketExists(thisBucket))
						client1.createBucket(thisBucket);
					added.add(thisBucket);
				}
			} catch (TooManyBucketsException e) {
				isc = true;
			}
			if (!isc)
				throw new NotThrowException();
		} finally {
			for (String s : added) {
				this.client1.deleteBucket(s);
			}
		}
	}

	@Test
	public void testGetBucket_1010() {
		this.ste = true;
		this.isc = false;
		try {
			client1.listObjects(bucket + "bucunzai");
		} catch (NoSuchBucketException e) {
			isc = true;
		}
		if (!isc)
			throw new NotThrowException();
	}

	@Test
	public void testGetBucket_1011() {
		String bucket1 = bucket + ".001";
		String bucket2 = bucket + ".002";
		String bucket3 = bucket + ".003";
		CreateBucketRequest request1 = new CreateBucketRequest(bucket1,
				CannedAccessControlList.Private);
		CreateBucketRequest request2 = new CreateBucketRequest(bucket2,
				CannedAccessControlList.PublicRead);
		CreateBucketRequest request3 = new CreateBucketRequest(bucket3,
				CannedAccessControlList.PublicReadWrite);
		if (client1.bucketExists(bucket1))
			client1.deleteBucket(bucket1);
		if (client1.bucketExists(bucket2))
			client1.deleteBucket(bucket2);
		if (client1.bucketExists(bucket3))
			client1.deleteBucket(bucket3);
		client1.createBucket(request1);
		client1.createBucket(request2);
		client1.createBucket(request3);
		System.out.println(client1.listObjects(bucket1));
		System.out.println(client1.listObjects(bucket2));
		System.out.println(client1.listObjects(bucket3));
		client1.deleteBucket(bucket1);
		client1.deleteBucket(bucket2);
		client1.deleteBucket(bucket3);
	}

	@Test
	public void testGetBucket_1012() {
		String bucket1 = bucket + ".001";
		String bucket2 = bucket + ".002";
		String bucket3 = bucket + ".003";
		CreateBucketRequest request1 = new CreateBucketRequest(bucket1,
				CannedAccessControlList.Private);
		CreateBucketRequest request2 = new CreateBucketRequest(bucket2,
				CannedAccessControlList.PublicRead);
		CreateBucketRequest request3 = new CreateBucketRequest(bucket3,
				CannedAccessControlList.PublicReadWrite);
		if (client2.bucketExists(bucket1))
			client2.deleteBucket(bucket1);
		if (client2.bucketExists(bucket2))
			client2.deleteBucket(bucket2);
		if (client2.bucketExists(bucket3))
			client2.deleteBucket(bucket3);
		client2.createBucket(request1);
		client2.createBucket(request2);
		client2.createBucket(request3);
		try {
			this.ste = true;
			this.isc = false;
			try {
				client1.listObjects(bucket1);
			} catch (AccessDeniedException e) {
				isc = true;
			}

			if (!isc)
				throw new NotThrowException();
			client1.listObjects(bucket2);
			client1.listObjects(bucket3);
		} finally {
			if (client2.bucketExists(bucket1))
				client2.deleteBucket(bucket1);
			if (client2.bucketExists(bucket2))
				client2.deleteBucket(bucket2);
			if (client2.bucketExists(bucket3))
				client2.deleteBucket(bucket3);
		}
	}

	@Test
	public void testGetBucket_1013() throws Exception {
		try {
			if (!client1.bucketExists(bucket))
				client1.createBucket(bucket);
			PutObjectRequest request = new PutObjectRequest(bucket, "file.xml",
					this.getClass().getClassLoader()
							.getResourceAsStream("uploadtest.xml"), null);
			client1.putObject(request);
			ListObjectsRequest request1 = new ListObjectsRequest(bucket);
			request1.setMaxKeys(-1);
			ObjectListing listing = client1.listObjects(request1);
			if (listing.getObjectSummaries().size()
					+ listing.getCommonPrefixes().size() > 0)
				throw new Exception("返回的结果数不为0");
		} finally {
			client1.deleteObject(bucket, "file.xml");
			client1.deleteBucket(bucket);
		}
	}
}
