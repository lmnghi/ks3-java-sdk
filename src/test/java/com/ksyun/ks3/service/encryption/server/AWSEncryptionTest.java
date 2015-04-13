package com.ksyun.ks3.service.encryption.server;

import org.junit.Before;

import com.ksyun.ks3.config.AWSConfigLoader;
import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月13日 下午4:59:27
 * 
 * @description 
 **/
public class AWSEncryptionTest {
	protected Ks3 client;
	@Before
	public void init(){
		ClientConfig.addConfigLoader(new AWSConfigLoader());
		client = new Ks3Client();
	}
}
