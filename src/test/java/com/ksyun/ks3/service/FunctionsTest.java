package com.ksyun.ks3.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.serviceside.BucketAlreadyExistsException;
import com.ksyun.ks3.service.request.CreateBucketRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月7日 下午1:54:21
 * 
 * @description
 **/
public class FunctionsTest extends Ks3ClientTest {
	/**
	 * 这个测试当bucket存在时新建抛出
	 * {@link BucketAlreadyExistsException}
	 * 否则新建成功
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateBucket_01() throws Exception {
		String bucketname = "lijunwei.sdk.test";
		
		if(this.client.bucketExists(bucketname))
		{
		    ste = true;
		    isc = false;
			try{
			    CreateBucketRequest request = new CreateBucketRequest(bucketname);
			    this.client.createBucket(request);
			}catch(BucketAlreadyExistsException ex){
				isc = true;
			} 
			if(!isc)
				throw new NotThrowException();
		}
		else{
			CreateBucketRequest request = new CreateBucketRequest(bucketname);
			this.client.createBucket(request);
		}
	}
	/**
	 * bucket名称中特殊字符的校验
	 */
	@Test
	public void testCreateBucket_02()
	{
		List<String> skiped = new ArrayList<String>();
		String [] bucketNames = new String [] {"AAAAAAA","aaa__","aaaaa$$","dddd|||","---dj3","...ddhhs","2","192.168.0.1","ksswe"};
		for(int i = 0;i< bucketNames.length;i++)
		{
			ste = true;
		    isc = false;
			try{
			    this.client.createBucket(bucketNames[i]);
			}catch(Ks3ClientException e)
			{
				isc = true;
			}
			if(isc == false)
			{
				skiped.add(bucketNames[i]);
			}
		}
		for(String s:skiped)
			client.deleteBucket(s);
		System.out.println(skiped);
		if(skiped.size()!=0)
			throw new NotThrowException();
	}
}
