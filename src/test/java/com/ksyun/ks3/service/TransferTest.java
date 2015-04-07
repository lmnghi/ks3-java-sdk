package com.ksyun.ks3.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.service.transfer.Ks3UploadClient;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月3日 下午5:26:25
 * 
 * @description 
 **/
public class TransferTest extends Ks3ClientTest{
	Ks3UploadClient upClient = null;
	@Before
	public void initupClient(){
		upClient = new Ks3UploadClient(super.client1,5,20,40);
	}
	String bucket = "test-transfer-client";
	@Test
	public void uploadDir(){
		Map<String, File> result = upClient.uploadDir(bucket,"windows/",new File("D:\\Program Files"));
		List<String>  error = upClient.checkDir(bucket, "windows/", new File("D:\\Program Files"));
		System.out.println(result);
		System.out.println(error);
	}
}
