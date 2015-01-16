package com.ksyun.ks3.service.object;

import org.junit.Test;

import com.ksyun.ks3.service.ObjectBeforeTest;

/**
 * 
 * @description 
 *
 * @author ZhangZhengyong [zhangzhengyong@kingsoft.com]
 * @dateTime 2015年1月14日  下午4:59:07
 *
 */
public class DeleteObjectTest extends ObjectBeforeTest {
	@Test
	public void deleteObject2001(){
		try{
			client.getObject(bucket, "deleteObject2001");
		}catch(Exception e){
			client.copyObject(bucket, "deleteObject2001", bucket, "hosts.txt");
		}
		client.deleteObject(bucket, "deleteObject2001");
	}
}
