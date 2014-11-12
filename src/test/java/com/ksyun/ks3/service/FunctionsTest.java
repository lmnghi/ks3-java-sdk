package com.ksyun.ks3.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.serviceside.BucketAlreadyExistsException;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.response.ListBucketsResponse;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月7日 下午1:54:21
 * 
 * @description
 **/
public class FunctionsTest extends Ks3ClientTest {
	/**
	 * 
	 * <p>
	 * 列出所有bucket list1
	 * </p>
	 * <p>
	 * 创建若干bucket list2
	 * </p>
	 * <p>
	 * 列出所有bucket list3
	 * </p>
	 * <p>
	 * list1+list2应该等于list3
	 * </p>
	 * 
	 * @throws Exception
	 */
	//@Test
	public void testGetService() throws Exception {
		List<Bucket> buckets = client1.listBuckets();
		List<Bucket> added = new ArrayList<Bucket>();
		try {
			for (int i = 0; i < 3; i++) {
				this.client1.createBucket("test.ljw." + i);
				added.add(new Bucket("test.ljw." + i));
			}
			List<Bucket> bucketsFinal = this.client1.listBuckets();
			buckets.addAll(added);

			if (buckets.size() != bucketsFinal.size()) {
				throw new Exception("Get Service Exception");
			}
			for (Bucket b : buckets) {
				if (!bucketsFinal.contains(b))
					throw new Exception("Get Service Exception");
			}
		} finally {
			for (Bucket b : added) {
				client1.deleteBucket(b.getName());
			}
		}
	}

	/**
	 * 这个测试当bucket存在时新建抛出 {@link BucketAlreadyExistsException} 否则新建成功
	 * 
	 * @throws Exception
	 */
	//@Test
	public void testCreateBucket_01() throws Exception {
		String bucketname = "lijunwei.sdk.test";

		if (this.client1.bucketExists(bucketname)) {
			ste = true;
			isc = false;
			try {
				CreateBucketRequest request = new CreateBucketRequest(
						bucketname);
				this.client1.createBucket(request);
			} catch (BucketAlreadyExistsException ex) {
				isc = true;
			}
			if (!isc)
				throw new NotThrowException();
		} else {
			CreateBucketRequest request = new CreateBucketRequest(bucketname);
			this.client1.createBucket(request);
			if (!this.client1.bucketExists(bucketname)) {
				throw new Exception("创建bucket失败，但是却返回正确");
			} else {
				client1.deleteBucket(bucketname);
			}
		}
	}

	/**
	 * bucket名称中特殊字符的校验
	 */
	//@Test
	public void testCreateBucket_02() {
		List<String> skiped = new ArrayList<String>();
		String[] bucketNames = new String[] { "AAAAAAA", "aaa__", "aaaaa$$",
				"dddd|||", "---dj3", "...ddhhs", "2", "192.168.0.1", "ksswe" };
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
}
