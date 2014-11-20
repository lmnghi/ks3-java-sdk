package com.ksyun.ks3.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ksyun.ks3.AutoAbortInputStream;
import com.ksyun.ks3.dto.AccessControlPolicy;
import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.exception.serviceside.AccessDeniedException;
import com.ksyun.ks3.exception.serviceside.NoSuchBucketException;
import com.ksyun.ks3.exception.serviceside.NoSuchKeyException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.Ks3CoreController;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.HeadObjectRequest;

/**
 * @description Tests about Object
 * 				测试内容包括权限测试和权限内功能测试两部分。
 * @author ZHANGZHENGYONG
 * @date 2014年11月17日  	20:30
 */
public class ObjectTest {
	private static Ks3 client;
	protected static Ks3 clientOther;
	private static Ks3CoreController controller;
	private static Authorization auth;
	protected static Authorization authOther;
	private static String bucket;
	private static Properties credential;
	
	private static Logger logger = LoggerFactory.getLogger(ObjectTest.class);
	/**
	 * 方法是否应该抛出异常
	 * shouldThrowException
	 */
	private boolean ste = false;
	/**
	 * 是否接受到异常
	 */
	private boolean isc = false;
	
	/**
	 * @description 加载类时调用，配置测试客户端数据，权限数据，和基础数据信息。
	 * 		
	 */
	@BeforeClass
	public static void beforeClass(){
		credential = new Properties();
		String accesskeyId1 = null;
		String accesskeySecret1 = null;
		String accesskeyId2 = null;
		String accesskeySecret2 = null;
		try {
			credential.load(ObjectTest.class.getResourceAsStream("accesskey.properties"));// resourece 路径存在问题 NullPointerException
			accesskeyId1 = credential.getProperty(accesskeyId1);
			accesskeySecret1 = credential.getProperty(accesskeySecret1);
			accesskeyId2 = credential.getProperty(accesskeyId2);
			accesskeySecret2 = credential.getProperty(accesskeySecret2);
			
		} catch (Exception e) {
			logger.warn("Error massage : " + e.toString());
			accesskeyId1 = "2HITWMQXL2VBB3XMAEHQ";
			accesskeySecret1 = "ilZQ9p/NHAK1dOYA/dTKKeIqT/t67rO6V2PrXUNr";
			accesskeyId2 = "GENJ6O5PQFVE37MEEMZA";
			accesskeySecret2 = "9Z6VbeYUJ0BiKcuwYe5x/j76TZvYe9VRh2OdH15m";
			
		}

		client = new Ks3Client(accesskeyId1,accesskeySecret1);
		auth = new Authorization(accesskeyId1,accesskeySecret1);
		
		clientOther = new Ks3Client(accesskeyId2,accesskeySecret2);
		authOther = new Authorization(accesskeyId2,accesskeySecret2);
		
		controller = new Ks3CoreController();
		
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
	
	/**
	 * @tag 功能测试
	 * @Test 错误的 key 或者无效的  bucketName, 抛出  NoSuchKeyException 异常
	 * @expected 抛出  NoSuchKeyException 异常.
	 * @Then {@value 若是输入不存在的bucketName, 是否应该先检查bucket的有效性，给出正确的提示}
	 */
	@Test(expected = NoSuchKeyException.class)
	public void getObjectTest1001(){

		client.getObject(bucket, "hello");

	}
	
	/**
	 * @tag 功能测试
	 * @Test 正确的 key 以及  bucketName
	 * @Then {@value 若是输入内容为文件夹名称 则抛出  NoSuchKeyException 异常, 若输入内容为文件名称则正常返回}
	 */
	@Test(expected = NoSuchKeyException.class)
	public void getObjectTest1002(){
		//输入为文件名，运行正常
		GetObjectResult object = client.getObject(bucket, "hosts.txt");
		System.out.println(object);
		
		//输入为文件夹名， 抛出 NoSuchKeyException
		try{
			client.getObject(bucket, "world");
		}catch(NoSuchKeyException e){
			throw e;
		}
	}
	
	/**
	 * @throws IOException 
	 * @tag 功能测试
	 * @Test 测试文件输出，获取相应文件。
	 * @Then {@value 有时会出现异常}
	 */
	@Test(timeout=20000)
	public void getObjectTest1003() throws IOException {
		GetObjectResult object = client.getObject(bucket, "hosts.txt");
		
		File file = new File("D:/objectTest/getObjectTest1003.txt");
		FileOutputStream fos = null;

		if (!file.exists()) {
			file.createNewFile();
		}

		AutoAbortInputStream is = object.getObject().getObjectContent();
		fos = new FileOutputStream(file);
		int ch;
		while ((ch = is.read()) != -1) {
			fos.write(ch);
		}
		
		fos.close();
		is.close();
	}
	
	/**
	 * @throws IOException 
	 * @tag 功能测试
	 * @Test 设置range范围，输出指定范围的文件内容
	 * @Then 
	 */
	@Test(timeout=10000)
	public void getObjectTest1004() throws IOException{
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		
		request.setRange(0, 299);
		GetObjectResult object = client.getObject(request);
		
		
		assertEquals(300, object.getObject().getObjectMetadata().getContentLength());
		File file = new File("D:/objectTest/getObjectTest1004.txt");

		FileOutputStream fos = null;
		
		if(!file.exists()){
			file.createNewFile();
		}
		fos = new FileOutputStream(file);
		AutoAbortInputStream is = object.getObject().getObjectContent();
		int ch;
		while((ch = is.read())!=-1){
			fos.write(ch);
		}
		
		is.close();
		fos.close();
		System.out.println(object);
	}
	
	/**
	 * @tag 功能测试
	 * @DoTask
	 * @Test 关于modify属性的测试 
	 * @Then 
	 */
	@Test()
	public void getObjectTest1005(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		
		Calendar cal = Calendar.getInstance();
		request.setModifiedSinceConstraint(cal.getTime());
		request.setUnmodifiedSinceConstraint(cal.getTime());
		
		GetObjectResult object = client.getObject(request);
		
		assertTrue(object.isIfModified());
		assertTrue(object.isIfPreconditionSuccess());
	}
	
	/**
	 * @tag 权限测试
	 * @Test 客户端访问非本用户文件---公共文件
	 * @Then 
	 */
	@Test
	public void getObjectTest1006(){
		GetObjectResult object = clientOther.getObject(bucket, "hostsPulbic.txt");
		System.out.println(object);
	}
	
	
	/**
	 * @tag 权限测试
	 * @Test 客户端访问非本用户文件---私密文件
	 * @Then 
	 */
	@Test(expected=AccessDeniedException.class)
	public void getObjectTest1007(){
		GetObjectResult object = clientOther.getObject(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 测试  GET Object
	 * @Test 
	 * @Then 
	 */
	@Test()
	public void getObjectTest(){
		GetObjectResult object = client.getObject(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	
	/**
	 * @tag 功能测试
	 * @Test 错误的 key 或者无效的  bucketName, 抛出  NoSuchKeyException 异常
	 * @expected 抛出  Ks3ServiceException 异常.
	 * @Then {@value 若是输入不存在的bucketName, 是否应该先检查bucket的有效性，给出正确的提示}
	 */
	@Test(expected = Ks3ServiceException.class)
	public void headObjectTest2001(){

		client.headObject(bucket, "hello");
//		client.headObject("123", "hello");

	}
	
	/**
	 * @tag 功能测试
	 * @Test 正确的 key 以及  bucketName
	 * @Then {@value 若是输入内容为文件夹名称 则抛出  Ks3ServiceException 异常, 若输入内容为文件名称则正常返回}
	 */
	@Test(expected = Ks3ServiceException.class)
	public void headObjectTest2002(){
		boolean flag = true;//true 代表为文件名，false 代表文件夹名
		if(flag){
			//输入为文件名，运行正常
			client.headObject(bucket, "hosts.txt");
			throw new NoSuchKeyException();
		} else {
			// 输入为文件夹名， 抛出 NoSuchKeyException
			try {
				client.headObject(bucket, "world");
			} catch (NoSuchKeyException e) {
				throw e;
			}
		}
	}
	
	/**
	 * @tag 功能测试
	 * @Test 测试内容，显示输出。
	 * @Then 
	 */
	@Test(timeout=5000)
	public void headObjectTest2003() {
		HeadObjectResult object = client.headObject(bucket, "hosts.txt");
		
		System.out.println(object);
	}
	
	/**
	 * @throws IOException 
	 * @tag 功能测试
	 * @Test 设置range范围，响应头部Content-Length与设置的值一致。
	 * @Then 
	 */
	@Test(timeout=5000)
	public void headObjectTest2004() throws IOException{
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		
		request.setRange(0, 299);
		
		HeadObjectResult result = client.headObject(request);
		
		System.out.println(result);
		
		System.out.println("getContentLength :" + result.getObjectMetadata().getContentLength());
		
		System.out.println("getContentLength :" + result.getObjectMetadata().getMeta(HttpHeaders.ContentLength.toString()));
	}
	
	/**
	 * @tag 功能测试
	 * @DoTask
	 * @Test 关于部分属性的测试 
	 * @Then 
	 */
	@Test()
	public void headObjectTest2005(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "hosts.txt");
		
		Calendar cal = Calendar.getInstance();
		request.setModifiedSinceConstraint(cal.getTime());
		request.setUnmodifiedSinceConstraint(cal.getTime());
		
		HeadObjectResult object = client.headObject(request);
		
		assertTrue(object.isIfModified());
		assertTrue(object.isIfPreconditionSuccess());
	}
	
	/**
	 * @tag 权限测试
	 * @Test 客户端访问非本用户文件---公共文件
	 * @Then 
	 */
	@Test
	public void headObjectTest2006(){
		HeadObjectResult object = clientOther.headObject(bucket, "hostsPulbic.txt");
		System.out.println(object);
	}
	
	
	/**
	 * @tag 权限测试
	 * @Test 客户端访问非本用户文件---私密文件
	 * @Then {@value 异常抛出问题， 403 权限问题}
	 */
	@Test(expected=Ks3ServiceException.class)
	public void headObjectTest2007(){
		HeadObjectResult object = clientOther.headObject(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 测试 HEAD Object
	 * @Test 错误的bucketName， 抛出 Ks3ServiceException 异常
	 * @Then {@value 是否应该抛出 NoSuchBucketException}
	 */
	@Test(expected=Ks3ServiceException.class)
	public void headObjectTest2008(){
		String noBucket = "notExist";
		HeadObjectResult object = client.headObject(noBucket, "hosts.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 测试 HEAD Object
	 * @Test 
	 * @Then 
	 */
	@Test
	public void headObjectTest(){
		HeadObjectResult object = client.headObject(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	
	/**
	 * @tag 功能测试
	 * @Test 正确的 key 以及  bucketName
	 * @Then 
	 */
	@Test
	public void getObjectACLTest3001(){
		AccessControlPolicy object = client.getObjectACL(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 功能测试
	 * @Test 错误的 key, 抛出  NoSuchKeyException 异常
	 * @Then 
	 */
	@Test(expected = NoSuchKeyException.class)
	public void getObjectACLTest3002(){
		AccessControlPolicy object = client.getObjectACL(bucket, "host.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 功能测试
	 * @Test 错误的 bucket, 抛出  NoSuchBucketException 异常
	 * @Then 
	 */
	@Test(expected = NoSuchBucketException.class)
	public void getObjectACLTest3003(){
		String tempbucket = "abc";
		AccessControlPolicy object = client.getObjectACL(tempbucket, "host.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 权限测试 
	 * @Test 客户端访问非本用户文件---公开文件
	 * @Then 
	 */
	@Test(expected=AccessDeniedException.class)
	public void getObjectACLTest3004(){

		AccessControlPolicy object = clientOther.getObjectACL(bucket, "hostsPulbic.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 权限测试 
	 * @Test 客户端访问非本用户文件---私密文件
	 * @Then 
	 */
	@Test(expected=AccessDeniedException.class)
	public void getObjectACLTest3005(){

		AccessControlPolicy object = clientOther.getObjectACL(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	/**
	 * @tag 测试  GET Object ACL
	 * @Test 打印对象内容
	 * @Then 
	 */
	@Test()
	public void getObjectACLTest(){

		AccessControlPolicy object = client.getObjectACL(bucket, "hosts.txt");
		System.out.println(object);
	}
	
	
}
