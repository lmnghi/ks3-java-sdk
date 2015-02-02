package com.ksyun.ks3.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.dto.CallBackConfiguration;
import com.ksyun.ks3.dto.CallBackConfiguration.MagicVariables;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.exception.serviceside.CallbackFailException;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午12:56:44
 * 
 * @description 
 **/
public class CallBackTest extends Ks3ClientTest{
	final String bucketName = "test-"+System.currentTimeMillis();
	final File file = new File(this.getClass().getClassLoader().getResource("git.exe").toString().substring(6));
	CallBackConfiguration config = new CallBackConfiguration();
	@Before
	public void createTestBucket(){
		client.createBucket(bucketName);
		
		config.setCallBackUrl("http://10.4.2.38:19090/");
		Map<String,MagicVariables> magicVariables = new HashMap<String,MagicVariables>();
		
		magicVariables.put("bucket", MagicVariables.bucket);
		magicVariables.put("createTime", MagicVariables.createTime);
		magicVariables.put("etag", MagicVariables.etag);
		magicVariables.put("key", MagicVariables.key);
		magicVariables.put("mimeType", MagicVariables.mimeType);
		magicVariables.put("objectSize", MagicVariables.objectSize);
		
		config.setBodyMagicVariables(magicVariables);
		
		Map<String,String> kssVariables = new HashMap<String,String>();
		
		kssVariables.put("user", "lijunwei");
		kssVariables.put("time", "20150222");
		kssVariables.put("location", "beijing");
		
		config.setBodyKssVariables(kssVariables);
	}
	@Test
	public void testPutObjectWithCallBack(){
		PutObjectRequest request = new PutObjectRequest(bucketName,"test",file);
		request.setCallBackConfiguration(config);
		client.putObject(request);
	}
	@Test(expected=CallbackFailException.class)
	public void testPutObjectWithCallBackFail(){
		PutObjectRequest request = new PutObjectRequest(bucketName,"test",file);	
		config.setCallBackUrl("http://10.4.2.38:19090/xx");
		request.setCallBackConfiguration(config);
		client.putObject(request);
	}
	@Test
	public void multipartUploadCallbackTest(){
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(bucketName, "test");
		
		UploadPartRequest request = new UploadPartRequest(result.getBucket(),result.getKey(),result.getUploadId(),1,file,file.length(),0);
		client.uploadPart(request);	
		
		ListPartsResult parts = client.listParts(bucketName, result.getKey(), result.getUploadId());

		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(parts);
		compRequest.setCallBackConfiguration(config);
		client.completeMultipartUpload(compRequest);
	}
	@Test(expected=CallbackFailException.class)
	public void multipartUploadCallbackTestFail(){
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(bucketName, "test");
		
		UploadPartRequest request = new UploadPartRequest(result.getBucket(),result.getKey(),result.getUploadId(),1,file,file.length(),0);
		client.uploadPart(request);	
		
		ListPartsResult parts = client.listParts(bucketName, result.getKey(), result.getUploadId());

		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(parts);
		config.setCallBackUrl("http://10.4.2.38:19090/xx");
		compRequest.setCallBackConfiguration(config);
		client.completeMultipartUpload(compRequest);
	}
	@After
	public void deleteBucket(){
		if(client.bucketExists(bucketName)){
			client.clearBucket(bucketName);
			client.deleteBucket(bucketName);
		}
	}

}
