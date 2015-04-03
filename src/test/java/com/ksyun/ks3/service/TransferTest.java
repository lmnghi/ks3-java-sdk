package com.ksyun.ks3.service;

import java.io.File;

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
		upClient = new Ks3UploadClient(super.client1,5,20);
	}
	String bucket = "test-transfer-client";
	@Test
	public void uploadDir(){
		System.out.println(upClient.uploadDir(bucket,"",new File("D://work\\workspace/.metadata")));
	}
	@Test
	public void mutipartUpload(){
		upClient.mutipartUploadByThreads(bucket,"test", new File("D://work\\workspace/.metadata/.bak_0.log"));
	}
}
