package com.ksyun.ks3.service;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月7日 下午1:46:05
 * 
 * @description
 **/
public class Ks3ClientTest {
	/**
	 * 方法是否应该抛出异常
	 * shouldThrowException
	 */
	protected boolean ste = false;
	/**
	 * 是否接受到异常
	 */
	protected boolean isc = false;
	protected Ks3 client;
	@Before
	public void init() throws IOException {

		final Properties credential = new Properties();
		credential.load(this.getClass().getClassLoader()
				.getResourceAsStream("accesskey.properties"));

		final String accesskeyId = credential.getProperty("accesskeyid");
		final String accesskeySecret = credential.getProperty("accesskeysecret");
		this.client = new Ks3Client(accesskeyId,accesskeySecret);
	}
}
