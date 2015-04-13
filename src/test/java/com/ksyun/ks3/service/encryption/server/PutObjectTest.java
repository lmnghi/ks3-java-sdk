package com.ksyun.ks3.service.encryption.server;

import java.io.File;

import org.junit.Test;

import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.SSECustomerKey;
import com.ksyun.ks3.service.request.PutObjectRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月13日 下午5:06:36
 * 
 * @description 
 **/
public class PutObjectTest extends AWSEncryptionTest{
	@Test
	public void test(){
		PutObjectRequest request = new PutObjectRequest(bucket,"test",new File("D://secret.key"));
		
		request.setSseCustomerKey(new SSECustomerKey(super.symKey));
/*		request.setCannedAcl(CannedAccessControlList.Private);
		request.setRedirectLocation("http://www.baidu.com");*/
		System.out.println(client.putObject(request));
	}
}
