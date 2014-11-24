package com.ksyun.ks3.service;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ksyun.ks3.dto.CopyResult;
import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.service.request.CopyObjectRequest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.HeadObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;

/**
 * @description	测试Object api中 request headers 的功能与逻辑
 * @author ZHANGZHENGYONG
 * @date 2014 年 11 月 24 日	11:22	
 */
public class ObjectHeadersTest extends ObjectTest {
	
	/**
	 * <p>The implementation of the <code>GET Object</code> can use the following 
	 * 	request headers in addition to the request headers common to all operations.
	 * <pre>
	 *  Range
	 *  If-Modified-Since
	 *  If-Unmodified-Since
	 *  If-Match
	 *  If-None-Match
	 * <pre></p>
	 */
	@Test(timeout=10000)
	public void getObjectHeaders(){
		GetObjectRequest request = new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		GetObjectRequest requestSec= new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		
		/* part range */
		//set the headers 
		request.setRange(0, 299);
		//get the result 
		GetObjectResult result = client.getObject(request);
		//assert the setting 
		assertEquals(300, result.getObject().getObjectMetadata().getContentLength());
		
		/* part Modified-Since */
		//set the headers
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		request.setModifiedSinceConstraint(date);
		requestSec.setUnmodifiedSinceConstraint(date);
		//get the result 
		result = client.getObject(request);
		GetObjectResult resultSec = client.getObject(requestSec);
		//assert the setting
//		assertEquals(result.isIfModified(), !resultSec.isIfModified());
	
		/* part If-Match */
		//set the headers
		List<String> eTags = this.getETags();
		List<String> eTagsSec = new ArrayList<String>();
		eTagsSec.add("21c49d6cf6400735433c02cb1b6c90f4");
		request.setMatchingETagConstraints(eTags);
		request.setNonmatchingEtagConstraints(eTags);
		//get the result 
		result = client.getObject(request);
		resultSec = client.getObject(requestSec);
		//assert the setting
		assertEquals(true, result.isIfPreconditionSuccess());
//		assertEquals(false, resultSec.isIfModified());
		System.out.println(result);
//		System.out.println(resultSec);
	}
	
	/**
	 * <code>getObjectHeaders</code>
	 * @part Range
	 */
	@Test(timeout=10000)
	public void getObjectHeaders2101(){
		GetObjectRequest request = new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		
		/* part range */
		//set the headers 
		request.setRange(0, 299);
		//get the result 
		GetObjectResult result = client.getObject(request);
		//assert the setting 
		assertEquals(300, result.getObject().getObjectMetadata().getContentLength());
		
	}
	
	/**
	 * <code>getObjectHeaders</code>
	 * @part Modified-Since
	 */
	@Test(timeout=10000)
	public void getObjectHeaders2102(){
		GetObjectRequest request = new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		GetObjectRequest requestSec= new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		
		/* part Modified-Since */
		//set the headers
		Calendar cal = Calendar.getInstance();
		Date date1 = cal.getTime();
		cal.add(Calendar.MONTH, -2);
		Date date2 = cal.getTime();
		request.setModifiedSinceConstraint(date1);
		requestSec.setUnmodifiedSinceConstraint(date2);
		//get the result 
		GetObjectResult result = client.getObject(request);
		GetObjectResult resultSec = client.getObject(requestSec);
		//assert the setting
		assertEquals(false, result.isIfModified());
		assertEquals(false, resultSec.isIfPreconditionSuccess());
		
		System.out.println("date1:"+date1+"\ndate2:"+date2);
		System.out.println(result+"\n"+resultSec);
	}
	
	/**
	 * <code>getObjectHeaders</code>
	 * @part If-Match
	 */
	@Test(timeout=10000)
	public void getObjectHeaders2103(){
		GetObjectRequest request = new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		GetObjectRequest requestSec= new GetObjectRequest(bucket, "headers/getObjectHeaders.txt");
		//set the headers
		List<String> eTags = this.getETags();
		List<String> eTagsSec = new ArrayList<String>();
		eTagsSec.add("21c49d6cf6400735433c02cb1b6c90f4");
		request.setMatchingETagConstraints(eTags);
		requestSec.setNonmatchingEtagConstraints(eTags);
		//get the result 
		GetObjectResult result = client.getObject(request);
		GetObjectResult resultSec = client.getObject(requestSec);
		//assert the setting
		assertEquals(true, result.isIfPreconditionSuccess());
		assertEquals(false, resultSec.isIfModified());
		
		request.setMatchingETagConstraints(eTagsSec);
		requestSec.setNonmatchingEtagConstraints(eTagsSec);
		//get the result 
		result = client.getObject(request);
		resultSec = client.getObject(requestSec);
		//assert the setting
		assertEquals(false, result.isIfPreconditionSuccess());
		assertEquals(true, resultSec.isIfModified());
		
//		System.out.println(result);
//		System.out.println(resultSec);
	}
	
	/**
	 * <p>The implementation of the <code>HEAD Object</code> can use the following 
	 * 	request headers in addition to the request headers common to all operations.
	 * <pre>
	 *  Range
	 *  If-Modified-Since
	 *  If-Unmodified-Since
	 *  If-Match
	 *  If-None-Match
	 * <pre></p>
	 */
	@Test(timeout=1500)
	public void headObjectHeaders(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "headers/getObjectHeaders.txt");
		
		HeadObjectResult result = client.headObject(request);
		System.out.println(result);
	}
	
	/**
	 * <code>headObjectHeaders</code>
	 * @part Range
	 */
	@Test(timeout=10000)
	public void headObjectHeaders2201(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "headers/headObjectHeaders.txt");
		
		/* part range */
		//set the headers 
		request.setRange(0, 299);
		//get the result 
		HeadObjectResult result = client.headObject(request);
		//assert the setting 
		assertEquals(300, result.getObjectMetadata().getContentLength());
		
	}
	
	/**
	 * <code>headObjectHeaders</code>
	 * @part Modified-Since
	 */
	@Test(timeout=10000)
	public void headObjectHeaders2202(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "headers/headObjectHeaders.txt");
		HeadObjectRequest requestSec= new HeadObjectRequest(bucket, "headers/headObjectHeaders.txt");
		
		/* part Modified-Since */
		//set the headers
		Calendar cal = Calendar.getInstance();
		Date date1 = cal.getTime();
		cal.add(Calendar.MONTH, -2);
		Date date2 = cal.getTime();
		request.setModifiedSinceConstraint(date1);
		requestSec.setUnmodifiedSinceConstraint(date2);
		//get the result 
		HeadObjectResult result = client.headObject(request);
		HeadObjectResult resultSec = client.headObject(requestSec);
		//assert the setting
		assertEquals(false, result.isIfModified());
		assertEquals(false, resultSec.isIfPreconditionSuccess());
		
		System.out.println("date1:"+date1+"\ndate2:"+date2);
		System.out.println(result+"\n"+resultSec);
		
	}
	
	/**
	 * <code>headObjectHeaders</code>
	 * @part If-Match
	 */
	@Test(timeout=10000)
	public void headObjectHeaders2203(){
		HeadObjectRequest request = new HeadObjectRequest(bucket, "headers/headObjectHeaders.txt");
		HeadObjectRequest requestSec= new HeadObjectRequest(bucket, "headers/headObjectHeaders.txt");
		//set the headers
		List<String> eTags = this.getETags();
		List<String> eTagsSec = new ArrayList<String>();
		eTagsSec.add("21c49d6cf6400735433c02cb1b6c90f4");
		request.setMatchingETagConstraints(eTags);
		requestSec.setNonmatchingEtagConstraints(eTags);
		//get the result 
		HeadObjectResult result = client.headObject(request);
		HeadObjectResult resultSec = client.headObject(requestSec);
		//assert the setting
		assertEquals(true, result.isIfPreconditionSuccess());
		assertEquals(false, resultSec.isIfModified());
		
		request.setMatchingETagConstraints(eTagsSec);
		requestSec.setNonmatchingEtagConstraints(eTagsSec);
		//get the result 
		result = client.headObject(request);
		resultSec = client.headObject(requestSec);
		//assert the setting
		assertEquals(false, result.isIfPreconditionSuccess());
		assertEquals(true, resultSec.isIfModified());
		
//		System.out.println(result);
//		System.out.println(resultSec);
		
	}
	
	
	/**
	 * <p>The implementation of the <code>DELETE Object</code> can use the request
	 *  header named <code>x-mfa</code>, but it is not implemented.
	 * </p>
	 */
	@Test(timeout=1500)
	public void deleteObjectHeaders(){
		
	}
	
	/**
	 * <p>The implementation of the <code>PUT Object</code> can use the following 
	 * 	request headers in addition to the request headers common to all operations.
	 * <pre>
	 *  Cache-Control
	 *  Content-Disposition
	 *  Content-Encoding
	 *  Content-Length
	 *  Content-MD5
	 *  Content-Type
	 *  Expect
	 *  Expires
	 *  redirect-location
	 * <pre></p>
	 */
	@Test(timeout=10000)
	public void putObjectHeaders(){
		PutObjectRequest request = new PutObjectRequest(bucket, "headers/putObjectHeaders.txt", new File("D:/objectTest/headers/putObjectHeaders.txt"));

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setCacheControl("no-cache");
		objectMeta.setContentDisposition("D:/");
		objectMeta.setContentEncoding("gb2312");
		objectMeta.setContentLength(123);
		objectMeta.setContentMD5("123dbPZABzVDPALLG2yQ8w==");
		objectMeta.setContentType("txt");
		objectMeta.setHttpExpiresDate(new Date());
		objectMeta.setHeader(HttpHeaders.Expect.toString(), "Expect: 100-continue");
		
		request.setObjectMeta(objectMeta);
		request.setRedirectLocation("http://ks3.ksyun.com/");
		client.putObject(request);
		
		GetObjectResult result = client.getObject(bucket, "headers/putObjectHeaders.txt");

		ObjectMetadata objMeta = result.getObject().getObjectMetadata();
		System.out.println("redirectLocation"+result.getObject().getRedirectLocation());
		System.out.println(objMeta);
	}
	
	
	/**
	 * <p>The implementation of the <code>PUT Object - Copy</code> can use the following 
	 * 	request headers in addition to the request headers common to all operations.
	 * <pre>
	 *  copy-source
	 *  metadata-directive
	 *  copy-source-if-match
	 *  copy-source-if-none-match
	 *  copy-source-if-unmodified-since
	 *  copy-source-if-modified-since
	 *  redirect-location
	 * <pre></p>
	 */
	@Test(timeout=1500)
	public void copyObjectHeaders(){
		CopyObjectRequest request = new CopyObjectRequest(bucket, "headers/getObjectHeaders.txt", "","");
		
		CopyResult result = client.copyObject(request);
		System.out.println(result);
	}
	
	/**
	 * <p>The implementation of the <code>Initiate Multipart Upload</code> can use the following 
	 * 	request headers in addition to the request headers common to all operations.
	 * <pre>
	 *  Cache-Control
	 *  Content-Disposition
	 *  Content-Encoding
	 *  Content-Type
	 *  Expires
	 *  redirect-location
	 * <pre></p>
	 */
	@Test(timeout=1500)
	public void initiateMultipartUpload(){
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, "headers/uploadObjectHeaders.txt");
		
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setCacheControl("no-cache");
		objectMeta.setContentDisposition("D:/");
		objectMeta.setContentEncoding("gb2312");
		objectMeta.setContentType("txt");
		objectMeta.setHttpExpiresDate(new Date());
		
		request.setObjectMeta(objectMeta);
		request.setRedirectLocation("http://ks3.ksyun.com/");
		client.initiateMultipartUpload(request);
		
		GetObjectResult result = client.getObject(bucket, "headers/putObjectHeaders.txt");

		System.out.println("redirectLocation"+result.getObject().getRedirectLocation());
		System.out.println(result);
	}
	
	/**
	 * <p>The implementation of the <code>Upload Part</code> can use the following 
	 * 	request headers in addition to the request headers common to all operations.
	 * <pre>
	 *  Content-Length
	 *  Content-MD5
	 *  Expect
	 * <pre></p>
	 */
	@Test(timeout=1500)
	public void uploadPart(){
//		PutObjectRequest request = new PutObjectRequest(bucket, "headers/getObjectHeaders.txt", new File("D:/objectTest/headers/headObjectHeaders"));
//		
//		PutObjectResult result = client.putObject(request);
//		System.out.println(result);
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
