package com.ksyun.ks3.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.dto.Fop;
import com.ksyun.ks3.service.request.PutPfopRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午6:18:56
 * 
 * @description 
 **/
public class PfopTest extends Ks3ClientTest{
	final String bucketName = "aaaaaaaavideo";
	final String key = "Policy.java";
	final File file = new File("D://"+key);
//	@Before
	public void createTestBucket(){
		if(!client.bucketExists(bucketName)){
			client.createBucket(bucketName);
		}
		try{
			client.headObject(bucketName, key);
		}catch(Exception e){
			client.putObject(bucketName,key,file);
		}
	}
	//@After
	public void deleteBucket(){
		if(client.bucketExists(bucketName)){
			client.clearBucket(bucketName);
			client.deleteBucket(bucketName);
		}
	}
	@Test
	public void testPutPfop(){
		PutPfopRequest request = new PutPfopRequest(bucketName,key);
		List<Fop> fops = new ArrayList<Fop>();
		
		Fop fop = new Fop();
		fop.setCommand("tag=avop&f=mp4&vbr=1000k&abr=64k&text=5rWL6K+V5Lit5paH&textlayout=NORTHWEST&font=5b6u6L2v6ZuF6buR&fontcolor=Silver&fontsize=30&textwp=10&texthp=10");
		fops.add(fop);
		
		request.setFops(fops);
		request.setNotifyURL("http://10.4.2.38:19090/");
		System.out.println(client.putPfopTask(request));
	}
	@Test
	public void testGetPfop(){
		System.out.println(client.getPfopTask("00P38MFhpAq3"));
	}
}
