package com.ksyun.ks3.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.AccessControlPolicy;
import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.CreateBucketConfiguration;
import com.ksyun.ks3.dto.CreateBucketConfiguration.REGION;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.Grantee;
import com.ksyun.ks3.dto.GranteeId;
import com.ksyun.ks3.dto.GranteeUri;
import com.ksyun.ks3.dto.HeadBucketResult;
import com.ksyun.ks3.dto.ListMultipartUploadsResult;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.exception.serviceside.AccessDeniedException;
import com.ksyun.ks3.exception.serviceside.BucketAlreadyExistsException;
import com.ksyun.ks3.exception.serviceside.BucketNotEmptyException;
import com.ksyun.ks3.exception.serviceside.InvalidLocationConstraintException;
import com.ksyun.ks3.exception.serviceside.NoSuchBucketException;
import com.ksyun.ks3.exception.serviceside.TooManyBucketsException;
import com.ksyun.ks3.request.ErrorCannedAclCreateBucketRequest;
import com.ksyun.ks3.request.ErrorCannedAclPutBucketAclRequest;
import com.ksyun.ks3.request.ErrorRegionCreateBucketRequest;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.HeadBucketRequest;
import com.ksyun.ks3.service.request.ListMultipartUploadsRequest;
import com.ksyun.ks3.service.request.ListObjectsRequest;
import com.ksyun.ks3.service.request.PutBucketACLRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.response.CreateBucketResponse;
import com.ksyun.ks3.service.response.PutBucketACLResponse;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
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
			} catch (IllegalArgumentException e) {
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
			this.controller.execute(auth1,
					new ErrorCannedAclCreateBucketRequest(bucket, "PublicR"),
					CreateBucketResponse.class);
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
			client1.putObject(bucket, "dir/", new ByteArrayInputStream(
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

	@Test
	public void testGetBucket_1014() throws Exception {
		try {
			if (!client1.bucketExists(bucket))
				client1.createBucket(bucket);
			PutObjectRequest request = new PutObjectRequest(bucket, "file.xml",
					this.getClass().getClassLoader()
							.getResourceAsStream("uploadtest.xml"), null);
			client1.putObject(request);
			ListObjectsRequest request1 = new ListObjectsRequest(bucket);
			request1.setMarker("file.xmlx");
			ObjectListing listing = client1.listObjects(request1);
			if (listing.getObjectSummaries().size()
					+ listing.getCommonPrefixes().size() > 0)
				throw new Exception("返回的结果数不为0");
		} finally {
			client1.deleteObject(bucket, "file.xml");
			client1.deleteBucket(bucket);
		}
	}

	@Test
	public void testGetBucketAcl_1015() throws Exception {
		if (client1.bucketExists(bucket)) {
			this.client1.clearBucket(bucket);
			this.client1.deleteBucket(bucket);
		}
		this.ste = true;
		this.isc = false;
		try {
			client1.getBucketACL(bucket);
		} catch (NoSuchBucketException e) {
			isc = true;
		}

		if (!isc)
			throw new NotThrowException();
	}

	@Test
	public void testGetBucketAcl_1016() throws Exception {
		if (client1.bucketExists(bucket + ".01")) {
			this.client1.clearBucket(bucket + ".01");
			this.client1.deleteBucket(bucket + ".01");
		}
		if (client1.bucketExists(bucket + ".02")) {
			this.client1.clearBucket(bucket + ".02");
			this.client1.deleteBucket(bucket + ".02");
		}
		if (client1.bucketExists(bucket + ".03")) {
			this.client1.clearBucket(bucket + ".03");
			this.client1.deleteBucket(bucket + ".03");
		}
		try {
			CreateBucketRequest request1 = new CreateBucketRequest(bucket
					+ ".01");
			request1.setCannedAcl(CannedAccessControlList.Private);
			CreateBucketRequest request2 = new CreateBucketRequest(bucket
					+ ".02");
			request2.setCannedAcl(CannedAccessControlList.PublicRead);
			CreateBucketRequest request3 = new CreateBucketRequest(bucket
					+ ".03");
			request3.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.createBucket(request1);
			client1.createBucket(request2);
			client1.createBucket(request3);
			client1.getBucketACL(bucket + ".01");
			client1.getBucketACL(bucket + ".02");
			client1.getBucketACL(bucket + ".03");
		} finally {
			if (client1.bucketExists(bucket + ".01")) {
				this.client1.clearBucket(bucket + ".01");
				this.client1.deleteBucket(bucket + ".01");
			}
			if (client1.bucketExists(bucket + ".02")) {
				this.client1.clearBucket(bucket + ".02");
				this.client1.deleteBucket(bucket + ".02");
			}
			if (client1.bucketExists(bucket + ".03")) {
				this.client1.clearBucket(bucket + ".03");
				this.client1.deleteBucket(bucket + ".03");
			}
		}
	}

	@Test
	public void testGetBucketAcl_1017() throws Exception {
		if (client1.bucketExists(bucket + ".01")) {
			this.client1.clearBucket(bucket + ".01");
			this.client1.deleteBucket(bucket + ".01");
		}
		if (client1.bucketExists(bucket + ".02")) {
			this.client1.clearBucket(bucket + ".02");
			this.client1.deleteBucket(bucket + ".02");
		}
		if (client1.bucketExists(bucket + ".03")) {
			this.client1.clearBucket(bucket + ".03");
			this.client1.deleteBucket(bucket + ".03");
		}
		try {
			CreateBucketRequest request1 = new CreateBucketRequest(bucket
					+ ".01");
			request1.setCannedAcl(CannedAccessControlList.Private);
			CreateBucketRequest request2 = new CreateBucketRequest(bucket
					+ ".02");
			request2.setCannedAcl(CannedAccessControlList.PublicRead);
			CreateBucketRequest request3 = new CreateBucketRequest(bucket
					+ ".03");
			request3.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.createBucket(request1);
			client1.createBucket(request2);
			client1.createBucket(request3);
			this.ste = true;
			this.isc = false;
			try {
				client2.getBucketACL(bucket + ".01");
			} catch (AccessDeniedException e) {
				isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.ste = true;
			this.isc = false;
			try {
				client2.getBucketACL(bucket + ".02");
			} catch (AccessDeniedException e) {
				isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client2.getBucketACL(bucket + ".03");
		} finally {
			if (client1.bucketExists(bucket + ".01")) {
				this.client1.clearBucket(bucket + ".01");
				this.client1.deleteBucket(bucket + ".01");
			}
			if (client1.bucketExists(bucket + ".02")) {
				this.client1.clearBucket(bucket + ".02");
				this.client1.deleteBucket(bucket + ".02");
			}
			if (client1.bucketExists(bucket + ".03")) {
				this.client1.clearBucket(bucket + ".03");
				this.client1.deleteBucket(bucket + ".03");
			}
		}
	}

	@Test
	public void testPutBucketAcl_1018() throws Exception {
		if (client1.bucketExists(bucket)) {
			this.client1.clearBucket(bucket);
			this.client1.deleteBucket(bucket);
		}
		try {
			CreateBucketRequest request = new CreateBucketRequest(bucket);
			request.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request);
			AccessControlPolicy policy1 = client1.getBucketACL(bucket);
			controller.execute(auth1, new ErrorCannedAclPutBucketAclRequest(
					bucket), PutBucketACLResponse.class);
			AccessControlPolicy policy2 = client1.getBucketACL(bucket);
			if (policy1.getAccessControlList().getGrants().size() < policy2
					.getAccessControlList().getGrants().size())
				throw new Exception("非法的canned acl生效了");

			PutBucketACLRequest request1 = new PutBucketACLRequest(bucket);
			request1.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(request1);
			AccessControlPolicy policy3 = client1.getBucketACL(bucket);
			List<String> grants = new ArrayList<String>();
			if (policy3.getAccessControlList().getGrants().size() == 2) {
				for (Grant grant : policy3.getAccessControlList().getGrants()) {
					grants.add(grant.getGrantee().getIdentifier()
							+ grant.getPermission());
				}
			} else
				throw new Exception("没有生效");
			if (!grants.contains(GranteeUri.AllUsers.getIdentifier()
					+ Permission.Read))
				throw new Exception("没有生效");

			PutBucketACLRequest request2 = new PutBucketACLRequest(bucket);
			request2.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(request2);
			AccessControlPolicy policy4 = client1.getBucketACL(bucket);
			List<String> grants1 = new ArrayList<String>();
			if (policy4.getAccessControlList().getGrants().size() == 3) {
				for (Grant grant : policy4.getAccessControlList().getGrants()) {
					grants1.add(grant.getGrantee().getIdentifier()
							+ grant.getPermission());
				}
			} else
				throw new Exception("没有生效");
			if (!grants1.contains(GranteeUri.AllUsers.getIdentifier()
					+ Permission.Read)
					|| !grants1.contains(GranteeUri.AllUsers.getIdentifier()
							+ Permission.Write))
				throw new Exception("没有生效");
		} finally {
			if (client1.bucketExists(bucket)) {
				this.client1.clearBucket(bucket);
				this.client1.deleteBucket(bucket);
			}
		}
	}

	@Test
	public void testPutBucketAcl_1019() throws Exception {
		if (client1.bucketExists(bucket)) {
			this.client1.clearBucket(bucket);
			this.client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);

			AccessControlList acl = new AccessControlList();
			acl.addGrant(GranteeUri.AllUsers, Permission.Read);
			Grantee grantee = new GranteeId();
			grantee.setIdentifier("123456");
			acl.addGrant(grantee, Permission.Read);
			PutBucketACLRequest request = new PutBucketACLRequest(bucket);
			request.setAccessControlList(acl);
			client1.putBucketACL(request);

			AccessControlPolicy aclPolicy = client1.getBucketACL(bucket);
			if (aclPolicy.getGrants().size() == 3) {
				throw new Exception("设置acl不应该成功，但是成功了");
			}
		} finally {
			if (client1.bucketExists(bucket)) {
				this.client1.clearBucket(bucket);
				this.client1.deleteBucket(bucket);
			}
		}
	}

	@Test
	public void testPutBucketAcl_1020() throws Exception {
		if (client1.bucketExists(bucket)) {
			this.client1.clearBucket(bucket);
			this.client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);

			AccessControlList acl = new AccessControlList();
			Grantee grantee = new GranteeId();
			grantee.setIdentifier("123456");
			acl.addGrant(grantee, Permission.Read);
			PutBucketACLRequest request = new PutBucketACLRequest(bucket);
			request.setAccessControlList(acl);
			client1.putBucketACL(request);
			AccessControlPolicy policy = client1.getBucketACL(bucket);
			if (policy.getGrants().size() != 2) {
				throw new Exception("授权没有生效");
			}

			AccessControlList acl1 = new AccessControlList();
			Grantee grantee1 = new GranteeId();
			grantee1.setIdentifier("123456");
			acl1.addGrant(grantee1, Permission.Read);
			PutBucketACLRequest request1 = new PutBucketACLRequest(bucket);
			request1.setAccessControlList(acl1);
			client1.putBucketACL(request1);
			AccessControlPolicy policy1 = client1.getBucketACL(bucket);
			if (policy1.getGrants().size() != 2) {
				throw new Exception("授权没有覆盖");
			}

			AccessControlList acl11 = new AccessControlList();
			Grantee grantee11 = new GranteeId();
			grantee11.setIdentifier("123456");
			acl11.addGrant(grantee11, Permission.FullControl);
			PutBucketACLRequest request11 = new PutBucketACLRequest(bucket);
			request11.setAccessControlList(acl11);
			client1.putBucketACL(request11);
			AccessControlPolicy policy11 = client1.getBucketACL(bucket);
			if (policy11.getGrants().size() != 2) {
				throw new Exception("授权没有覆盖");
			}
		} finally {
			if (client1.bucketExists(bucket)) {
				this.client1.clearBucket(bucket);
				this.client1.deleteBucket(bucket);
			}
		}
	}

	@Test
	public void testHeadBucket_1021() throws Exception {
		if (client1.bucketExists(bucket + ".01")) {
			this.client1.clearBucket(bucket + ".01");
			this.client1.deleteBucket(bucket + ".01");
		}
		if (client1.bucketExists(bucket + ".02")) {
			this.client1.clearBucket(bucket + ".02");
			this.client1.deleteBucket(bucket + ".02");
		}
		if (client1.bucketExists(bucket + ".03")) {
			this.client1.clearBucket(bucket + ".03");
			this.client1.deleteBucket(bucket + ".03");
		}
		if (client1.bucketExists(bucket + ".04")) {
			this.client1.clearBucket(bucket + ".04");
			this.client1.deleteBucket(bucket + ".04");
		}
		if (client1.bucketExists(bucket + ".05")) {
			this.client1.clearBucket(bucket + ".05");
			this.client1.deleteBucket(bucket + ".05");
		}
		if (client1.bucketExists(bucket + ".06")) {
			this.client1.clearBucket(bucket + ".06");
			this.client1.deleteBucket(bucket + ".06");
		}
		if (client1.bucketExists(bucket + ".07")) {
			this.client1.clearBucket(bucket + ".07");
			this.client1.deleteBucket(bucket + ".07");
		}
		try {
			if (404 != client1.headBucket(bucket + ".01").getStatueCode())
				throw new Exception("not 404");

			CreateBucketRequest request1 = new CreateBucketRequest(bucket
					+ ".02");
			request1.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request1);
			if (200 != client1.headBucket(bucket + ".02").getStatueCode())
				throw new Exception("not 200");

			CreateBucketRequest request2 = new CreateBucketRequest(bucket
					+ ".03");
			request2.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.createBucket(request2);
			if (200 != client1.headBucket(bucket + ".03").getStatueCode())
				throw new Exception("not 200");

			CreateBucketRequest request3 = new CreateBucketRequest(bucket
					+ ".04");
			request1.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.createBucket(request3);
			if (200 != client1.headBucket(bucket + ".04").getStatueCode())
				throw new Exception("not 200");

			CreateBucketRequest request4 = new CreateBucketRequest(bucket
					+ ".05");
			request4.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request4);
			if (403 != client2.headBucket(bucket + ".05").getStatueCode())
				throw new Exception("not 403");

			CreateBucketRequest request5 = new CreateBucketRequest(bucket
					+ ".06");
			request5.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.createBucket(request5);
			if (200 != client2.headBucket(bucket + ".06").getStatueCode())
				throw new Exception("not 200");

			CreateBucketRequest request6 = new CreateBucketRequest(bucket
					+ ".07");
			request6.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.createBucket(request6);
			if (200 != client2.headBucket(bucket + ".07").getStatueCode())
				throw new Exception("not 200");

		} finally {
			if (client1.bucketExists(bucket + ".01")) {
				this.client1.clearBucket(bucket + ".01");
				this.client1.deleteBucket(bucket + ".01");
			}
			if (client1.bucketExists(bucket + ".02")) {
				this.client1.clearBucket(bucket + ".02");
				this.client1.deleteBucket(bucket + ".02");
			}
			if (client1.bucketExists(bucket + ".03")) {
				this.client1.clearBucket(bucket + ".03");
				this.client1.deleteBucket(bucket + ".03");
			}
			if (client1.bucketExists(bucket + ".04")) {
				this.client1.clearBucket(bucket + ".04");
				this.client1.deleteBucket(bucket + ".04");
			}
			if (client1.bucketExists(bucket + ".05")) {
				this.client1.clearBucket(bucket + ".05");
				this.client1.deleteBucket(bucket + ".05");
			}
			if (client1.bucketExists(bucket + ".06")) {
				this.client1.clearBucket(bucket + ".06");
				this.client1.deleteBucket(bucket + ".06");
			}
			if (client1.bucketExists(bucket + ".07")) {
				this.client1.clearBucket(bucket + ".07");
				this.client1.deleteBucket(bucket + ".07");
			}
		}
	}

	@Test
	public void testGetBucketLocation_1022() throws Exception {
		if (client1.bucketExists(bucket)) {
			this.client1.clearBucket(bucket);
			this.client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);
			if (!REGION.HANGZHOU.toString().equals(
					client1.getBucketLoaction(bucket).toString()))
				throw new Exception("地点不正确");
			client1.deleteBucket(bucket);

			CreateBucketRequest request = new CreateBucketRequest(bucket);
			CreateBucketConfiguration config = new CreateBucketConfiguration(
					REGION.JIYANG);
			request.setConfig(config);
			client1.createBucket(request);

			if (!REGION.JIYANG.toString().equals(
					client1.getBucketLoaction(bucket).toString()))
				throw new Exception("地点不正确");
		} finally {
			if (client1.bucketExists(bucket)) {
				this.client1.clearBucket(bucket);
				this.client1.deleteBucket(bucket);
			}
		}
	}

	@Test
	public void testPutBucketLogging_1023() throws Exception {
		if (client1.bucketExists(bucket + "1")) {
			this.client1.clearBucket(bucket + "1");
			this.client1.deleteBucket(bucket + "1");
		}
		if (client1.bucketExists(bucket + "2")) {
			this.client1.clearBucket(bucket + "2");
			this.client1.deleteBucket(bucket + "2");
		}
		try {
			CreateBucketRequest request1 = new CreateBucketRequest(bucket + "1");
			CreateBucketRequest request2 = new CreateBucketRequest(bucket + "2");

			request1.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request1);
			request2.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request2);

			client1.putBucketACL(bucket + "1", CannedAccessControlList.Private);

			client1.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicRead);

			client1.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicReadWrite);

			client1.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			client1.putBucketLogging(bucket + "1", true, bucket + "2");
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

		} finally {
			if (client1.bucketExists(bucket + "1")) {
				this.client1.clearBucket(bucket + "1");
				this.client1.deleteBucket(bucket + "1");
			}
			if (client1.bucketExists(bucket + "2")) {
				this.client1.clearBucket(bucket + "2");
				this.client1.deleteBucket(bucket + "2");
			}
		}
	}

	@Test
	public void testPutBucketLogging_1024() throws Exception {
		if (client1.bucketExists(bucket + "1")) {
			this.client1.clearBucket(bucket + "1");
			this.client1.deleteBucket(bucket + "1");
		}
		if (client1.bucketExists(bucket + "2")) {
			this.client1.clearBucket(bucket + "2");
			this.client1.deleteBucket(bucket + "2");
		}
		try {
			CreateBucketRequest request1 = new CreateBucketRequest(bucket + "1");
			CreateBucketRequest request2 = new CreateBucketRequest(bucket + "2");

			request1.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request1);
			request2.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request2);

			client1.putBucketACL(bucket + "1", CannedAccessControlList.Private);

			client1.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicRead);

			client1.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");

			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicReadWrite);

			client1.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

		} finally {
			if (client1.bucketExists(bucket + "1")) {
				this.client1.clearBucket(bucket + "1");
				this.client1.deleteBucket(bucket + "1");
			}
			if (client1.bucketExists(bucket + "2")) {
				this.client1.clearBucket(bucket + "2");
				this.client1.deleteBucket(bucket + "2");
			}
		}
	}

	@Test
	public void testPutBucketLogging_1025() throws Exception {
		if (client1.bucketExists(bucket + "1")) {
			this.client1.clearBucket(bucket + "1");
			this.client1.deleteBucket(bucket + "1");
		}
		if (client2.bucketExists(bucket + "2")) {
			this.client2.clearBucket(bucket + "2");
			this.client2.deleteBucket(bucket + "2");
		}
		try {
			CreateBucketRequest request1 = new CreateBucketRequest(bucket + "1");
			CreateBucketRequest request2 = new CreateBucketRequest(bucket + "2");

			request1.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request1);
			request2.setCannedAcl(CannedAccessControlList.Private);
			client2.createBucket(request2);

			client1.putBucketACL(bucket + "1", CannedAccessControlList.Private);

			client2.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicRead);

			client2.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");

			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicReadWrite);

			client2.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			try {
				client2.putBucketLogging(bucket + "1", false, bucket + "2");
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();

		} finally {
			if (client1.bucketExists(bucket + "1")) {
				this.client1.clearBucket(bucket + "1");
				this.client1.deleteBucket(bucket + "1");
			}
			if (client2.bucketExists(bucket + "2")) {
				this.client2.clearBucket(bucket + "2");
				this.client2.deleteBucket(bucket + "2");
			}
		}
	}

	@Test
	public void testPutBucketLogging_1026() {
		if (client1.bucketExists(bucket + "1")) {
			this.client1.clearBucket(bucket + "1");
			this.client1.deleteBucket(bucket + "1");
		}
		if (client2.bucketExists(bucket + "2")) {
			this.client2.clearBucket(bucket + "2");
			this.client2.deleteBucket(bucket + "2");
		}
		try {
			CreateBucketRequest request1 = new CreateBucketRequest(bucket + "1");
			CreateBucketRequest request2 = new CreateBucketRequest(bucket + "2");

			request1.setCannedAcl(CannedAccessControlList.Private);
			client1.createBucket(request1);
			request2.setCannedAcl(CannedAccessControlList.Private);
			client2.createBucket(request2);

			client1.putBucketACL(bucket + "1", CannedAccessControlList.Private);

			client2.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			client1.putBucketLogging(bucket + "1", false, bucket + "2");
			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			this.isc = false;
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicRead);

			client2.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client1.putBucketACL(bucket + "1",
					CannedAccessControlList.PublicReadWrite);

			client2.putBucketACL(bucket + "2", CannedAccessControlList.Private);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicRead);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");

			client2.putBucketACL(bucket + "2",
					CannedAccessControlList.PublicReadWrite);
			this.isc = false;
			try {
				client1.putBucketLogging(bucket + "1", true, bucket + "2");
			} catch (Ks3ServiceException e) {
				this.isc = true;
			}
			if (!isc)
				throw new NotThrowException();
			client1.putBucketLogging(bucket + "1", false, bucket + "2");
		} finally {
			if (client1.bucketExists(bucket + "1")) {
				this.client1.clearBucket(bucket + "1");
				this.client1.deleteBucket(bucket + "1");
			}
			if (client2.bucketExists(bucket + "2")) {
				this.client2.clearBucket(bucket + "2");
				this.client2.deleteBucket(bucket + "2");
			}
		}
	}

	@Test
	public void testGetBucketLogging_1027() {
		if (client1.bucketExists(bucket)) {
			client1.clearBucket(bucket);
			client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);

			PutBucketACLRequest request = new PutBucketACLRequest(bucket);
			request.setCannedAcl(CannedAccessControlList.Private);
			client1.putBucketACL(request);
			client1.getBucketLogging(bucket);

			request.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(request);
			client1.getBucketLogging(bucket);

			request.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(request);
			client1.getBucketLogging(bucket);
		} finally {
			if (client1.bucketExists(bucket)) {
				client1.clearBucket(bucket);
				client1.deleteBucket(bucket);
			}
		}
	}

	@Test
	public void testGetBucketLogging_1028() {
		if (client1.bucketExists(bucket)) {
			client1.clearBucket(bucket);
			client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);

			PutBucketACLRequest request = new PutBucketACLRequest(bucket);
			request.setCannedAcl(CannedAccessControlList.Private);
			client1.putBucketACL(request);
			this.isc = false;
			try {
				client2.getBucketLogging(bucket);
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if(!isc)
				throw new NotThrowException();

			request.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(request);
			this.isc = false;
			try {
				client2.getBucketLogging(bucket);
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if(!isc)
				throw new NotThrowException();

			request.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(request);
			try {
				client2.getBucketLogging(bucket);
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if(!isc)
				throw new NotThrowException();
		} finally {
			if (client1.bucketExists(bucket)) {
				client1.clearBucket(bucket);
				client1.deleteBucket(bucket);
			}
		}
	}
	@Test
	public void testGetBucketLocation_1029() {
		if (client1.bucketExists(bucket)) {
			client1.clearBucket(bucket);
			client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);

			PutBucketACLRequest request = new PutBucketACLRequest(bucket);
			request.setCannedAcl(CannedAccessControlList.Private);
			client1.putBucketACL(request);
			client1.getBucketLoaction(bucket);

			request.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(request);
			client1.getBucketLoaction(bucket);

			request.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(request);
			client1.getBucketLoaction(bucket);
		} finally {
			if (client1.bucketExists(bucket)) {
				client1.clearBucket(bucket);
				client1.deleteBucket(bucket);
			}
		}
	}

	@Test
	public void testGetBucketLocation_1030() {
		if (client1.bucketExists(bucket)) {
			client1.clearBucket(bucket);
			client1.deleteBucket(bucket);
		}
		try {
			client1.createBucket(bucket);

			PutBucketACLRequest request = new PutBucketACLRequest(bucket);
			request.setCannedAcl(CannedAccessControlList.Private);
			client1.putBucketACL(request);
			this.isc = false;
			try {
				client2.getBucketLoaction(bucket);
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if(!isc)
				throw new NotThrowException();

			request.setCannedAcl(CannedAccessControlList.PublicRead);
			client1.putBucketACL(request);
			this.isc = false;
			try {
				client2.getBucketLoaction(bucket);
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if(!isc)
				throw new NotThrowException();

			request.setCannedAcl(CannedAccessControlList.PublicReadWrite);
			client1.putBucketACL(request);
			try {
				client2.getBucketLoaction(bucket);
			} catch (AccessDeniedException e) {
				this.isc = true;
			}
			if(!isc)
				throw new NotThrowException();
		} finally {
			if (client1.bucketExists(bucket)) {
				client1.clearBucket(bucket);
				client1.deleteBucket(bucket);
			}
		}
	}
	@Test
	public void testListMultiPartUploads_1035() throws Exception{
		ListMultipartUploadsRequest request = new ListMultipartUploadsRequest("ksc-scm");
		request.setDelimiter("");
		request.setKeyMarker("");
		request.setMaxUploads(10);
		request.setPrefix("");
		request.setUploadIdMarker("56182c1cd7ae4718bb15fe6380e81d18");
		ListMultipartUploadsResult result = client1.listMultipartUploads(request);
		if(!"delimiter".equals(result.getDelimiter())){
			throw new Exception();
		}
		if(!"keyMarker".equals(result.getKeyMarker())){
			throw new Exception();
		}
		if(!"prefix".equals(result.getPrefix())){
			throw new Exception();
		}
		if(!"uploadIdMarker".equals(result.getUploadIdMarker())){
			throw new Exception();
		}
		if(result.getMaxUploads()!=10){
			throw new Exception();
		}
	}
}
