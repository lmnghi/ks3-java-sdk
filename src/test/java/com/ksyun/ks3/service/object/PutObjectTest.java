package com.ksyun.ks3.service.object;

import java.io.File;

import org.junit.Test;

import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.service.ObjectBeforeTest;
import com.ksyun.ks3.service.request.PutObjectRequest;

public class PutObjectTest extends ObjectBeforeTest {
	/**
	 * redirect location
	 */
	@Test
	public void putObject001(){
		PutObjectRequest request = new PutObjectRequest(bucket, "headObject3001", new File("D:/objectTest/record.txt"));
		request.setRedirectLocation("/anotherPage.html");
		client.putObject(request);
	}
	
	/**
	 * storage class
	 */
	@Test
	public void putObject002(){
		PutObjectRequest request = new PutObjectRequest(bucket, "headObject3001", new File("D:/objectTest/record.txt"));
//		request.setStorageClass("REDUCED_REDUNDANCY");
		client.putObject(request);
	}
	
	/**
	 * 
	 */
	@Test
	public void putObject004(){
		PutObjectRequest request = new PutObjectRequest(bucket, "headObject3001", new File("D:/objectTest/record.txt"));
		ObjectMetadata metadata = new ObjectMetadata();
		
	}
}
