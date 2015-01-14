package com.ksyun.ks3.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.http.Ks3CoreController;

public class ObjectBeforeTest {
	protected static Ks3 client;
	protected static Ks3 clientOther;
	protected static String bucket;
	
	private static Log logger = LogFactory.getLog(ObjectBeforeTest.class);
	
	/**
	 * @description 加载类时调用，配置测试客户端数据，权限数据，和基础数据信息。
	 * 		
	 */
	@BeforeClass
	public static void beforeClass(){
		Properties credential = new Properties();
		String accesskeyId1 = null;
		String accesskeySecret1 = null;
		String accesskeyId2 = null;
		String accesskeySecret2 = null;
		try {
			credential.load(ObjectTest.class.getResourceAsStream("/accesskey.properties"));// resourece 路径存在问题 NullPointerException
			accesskeyId1 = credential.getProperty("accesskeyid1");
			accesskeySecret1 = credential.getProperty("accesskeysecret1");
			accesskeyId2 = credential.getProperty("accesskeyid2");
			accesskeySecret2 = credential.getProperty("accesskeysecret2");
			
		} catch (Exception e) {
			logger.warn("Error massage : " + e.toString());
			accesskeyId1 = "";
			accesskeySecret1 = "";
			accesskeyId2 = "";
			accesskeySecret2 = "";
			
		}

		client = new Ks3Client(accesskeyId1,accesskeySecret1);
		
		clientOther = new Ks3Client(accesskeyId2,accesskeySecret2);
		
		new Ks3CoreController();
		
		bucket = "test1-zzy";
		File fileDir = new File("D:/objectTest");
		fileDir.mkdir();
	}
	
	/**
	 * @description 配置测试客户端数据，执行每一个测试用例前都会执行 @Before 中代码
	 */
	@AfterClass
	public static void afterClass(){
		
	}
	
	public List<String> getETags(){
		List<String> eTags = new ArrayList<String>();
		try{
			GetObjectResult result = client.getObject(bucket, "hosts.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
			
			result = client.getObject(bucket, "deleteTest.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
			
			result = client.getObject(bucket, "putObjectTest.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
			
			result = client.getObject(bucket, "putObjectTestP.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
			
			result = client.getObject(bucket, "hostsPulbic.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
			
			result = client.getObject(bucket, "headers/getObjectHeaders.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
			
			result = client.getObject(bucket, "headers/headObjectHeaders.txt");
			eTags.add(result.getObject().getObjectMetadata().getETag());
		}catch(Exception e){
			e.printStackTrace();
		}
		return eTags;
	}
	
}
