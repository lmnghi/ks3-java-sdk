package com.ksyun.ks3.service.object;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.service.ObjectBeforeTest;
import com.ksyun.ks3.service.request.HeadObjectRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;

public class HeadObjectTest extends ObjectBeforeTest {

	/**
	 * If-Modified-Since 304
	 */
	@Test
	public void getObject1011(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		request.setModifiedSinceConstraint(cal.getTime());
		System.out.println(client.headObject(request));
	}
	
	/**
	 * If-Modified-Since 200
	 */
	@Test
	public void getObject1012(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		request.setModifiedSinceConstraint(cal.getTime());
		System.out.println(client.headObject(request));
	}
	
	/**
	 * If-Unmodified-Since 200
	 */
	@Test
	public void getObject1013(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		request.setUnmodifiedSinceConstraint(cal.getTime());
		System.out.println(client.headObject(request));
	}
	
	/**
	 * If-Unmodified-Since 412
	 */
	@Test
	public void getObject1014(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		request.setUnmodifiedSinceConstraint(cal.getTime());
		System.out.println(client.headObject(request));
	}
	
	/**
	 * if-Match 412
	 */
	@Test
	public void getObject1015(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = new ArrayList<String>();
		eTagList.add("abc");
		request.setMatchingETagConstraints(eTagList);
		System.out.println(client.headObject(request));
	}
	
	/**
	 * if-Match 200
	 */
	@Test
	public void getObject1016(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = getETags();
		request.setMatchingETagConstraints(eTagList);
		System.out.println(client.headObject(request));
	}
	
	/**
	 * if-Match 200
	 */
	@Test
	public void getObject1017(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = new ArrayList<String>();
		eTagList.add("abc");
		request.setNonmatchingEtagConstraints(eTagList);
		System.out.println(client.headObject(request));
	}
	
	/**
	 * if-Match 304
	 */
	@Test
	public void getObject1018(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = getETags();
		request.setNonmatchingEtagConstraints(eTagList);
		System.out.println(client.headObject(request));
	}
	
	/**
	 * Range
	 */
	@Test
	public void getObject1019(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		request.setRange(0, 300);
		System.out.println(client.headObject(request));
	}
	
	@Test
	public void headObject1001(){
		PutObjectRequest request = new PutObjectRequest(bucket, "headObject3001", new File("D:/objectTest/record.txt"));
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setUserMeta("abc", "abc");
		
		request.setObjectMeta(metadata);
		
		client.putObject(request);
		System.out.println(client.getObject(bucket, "headObject3001").getObject().getObjectMetadata());
	}
}
