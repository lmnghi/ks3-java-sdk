package com.ksyun.ks3.service.object;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ksyun.ks3.dto.ResponseHeaderOverrides;
import com.ksyun.ks3.exception.serviceside.NoSuchKeyException;
import com.ksyun.ks3.service.ObjectBeforeTest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;

/**
 * 
 * @description <p>Request Parameters</p>
 *
 * @author ZhangZhengyong [zhangzhengyong@kingsoft.com]
 * @dateTime 2015年1月13日  下午6:38:33
 *
 */
public class GetObjectTest extends ObjectBeforeTest {
	
	@Test
	public void getObject1001(){
		client.getObject(bucket, "hosts.txt");
	}
	
	/**
	 * response-content-type
	 */
	@Test
	public void getObject1002(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentType("text/xml");
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	
	
	/**
	 * response-content-language
	 * 
	 * @throws InternalError
	 */
	@Test
	public void getObject1003(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentLanguage("zh-CN");
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	
	/**
	 * response-expires
	 * 
	 * @throws InternalError
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void getObject1004(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setExpires(new Date("Tue, 16 Nov 2014 03:59:37 GMT"));
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	
	/**
	 * response-cache-control
	 * 
	 * @throws InternalError
	 */
	@Test
	public void getObject1005(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setCacheControl("no-cache");
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	
	/**
	 * response-content-disposition
	 * 
	 * @throws InternalError
	 */
	@Test
	public void getObject1006(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentDisposition("D:/");
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	
	/**
	 * response-content-encoding
	 * 
	 * @throws InternalError
	 */
	@Test
	public void getObject1007(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentEncoding("gzip");
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	

	/**
	 * response-content-type
	 * response-content-language
	 * response-expires
	 * response-cache-control
	 * response-content-disposition
	 * response-content-encoding
	 * 
	 * @throws InternalError
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void getObject1008(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentType("text/xml");
//		responseHeaders.setContentLanguage("zh-CN");
		responseHeaders.setExpires(new Date("Tue, 16 Nov 2014 03:59:37 GMT"));
		responseHeaders.setCacheControl("no-cache");
		responseHeaders.setContentDisposition("D:/");
		responseHeaders.setContentEncoding("gzip");
		request.setOverrides(responseHeaders);
		client.getObject(request);
	}
	
	/**
	 * redirect location
	 */
	@Test
	public void getObject1009(){
		PutObjectRequest request = new PutObjectRequest(bucket, "put_test", new File("D:/objectTest/abc.txt"));
		request.setRedirectLocation("ks3.ksyun.com");
		client.putObject(request);
		System.out.println(client.getObject(bucket, "put_test"));
	}
	
	/**
	 * delete marker
	 * 
	 * <p>若已删除，api不会返回delete marker，而是返回NoSuchKey</p>
	 */
	@Test(expected=NoSuchKeyException.class)
	public void getObject1010(){
		client.getObject(bucket, "deleteMarker");
	}
	
	/**
	 * If-Modified-Since 304
	 */
	@Test
	public void getObject1011(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		request.setModifiedSinceConstraint(cal.getTime());
		System.out.println(client.getObject(request));
	}
	
	/**
	 * If-Modified-Since 200
	 */
	@Test
	public void getObject1012(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		request.setModifiedSinceConstraint(cal.getTime());
		System.out.println(client.getObject(request));
	}
	
	/**
	 * If-Unmodified-Since 200
	 */
	@Test
	public void getObject1013(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		request.setUnmodifiedSinceConstraint(cal.getTime());
		System.out.println(client.getObject(request));
	}
	
	/**
	 * If-Unmodified-Since 412
	 */
	@Test
	public void getObject1014(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		request.setUnmodifiedSinceConstraint(cal.getTime());
		System.out.println(client.getObject(request));
	}
	
	/**
	 * if-Match 412
	 */
	@Test
	public void getObject1015(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = new ArrayList<String>();
		eTagList.add("abc");
		request.setMatchingETagConstraints(eTagList);
		System.out.println(client.getObject(request));
	}
	
	/**
	 * if-Match 200
	 */
	@Test
	public void getObject1016(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = getETags();
		request.setMatchingETagConstraints(eTagList);
		System.out.println(client.getObject(request));
	}
	
	/**
	 * if-Match 200
	 */
	@Test
	public void getObject1017(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = new ArrayList<String>();
		eTagList.add("abc");
		request.setNonmatchingEtagConstraints(eTagList);
		System.out.println(client.getObject(request));
	}
	
	/**
	 * if-Match 304
	 */
	@Test
	public void getObject1018(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		List<String> eTagList = getETags();
		request.setNonmatchingEtagConstraints(eTagList);
		System.out.println(client.getObject(request));
	}
	
	/**
	 * Range
	 */
	@Test
	public void getObject1019(){
		GetObjectRequest request = new GetObjectRequest(bucket, "hosts.txt");
		request.setRange(0, 300);
		System.out.println(client.getObject(request));
	}
}
