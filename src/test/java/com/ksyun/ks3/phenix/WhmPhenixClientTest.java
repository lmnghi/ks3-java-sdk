package com.ksyun.ks3.phenix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.utils.Md5Utils;

/**
 * @author whm[wanghaiming1@kingsoft.com]  
 * 
 * @date 2014年10月15日 上午10:28:53
 * 
 * @description
 **/
public class WhmPhenixClientTest extends PhenixClientTest {

	@Test
	public void test() throws Ks3ServiceException, Ks3ClientException, IOException {
//		client.createBucket("murongyifei");//只创建一次,相当于/home/murongyifei这样的概念,   如果有拒绝创建,409 Conflict代表已经存在
		
		client.putObject("murongyifei", "test1", new FileInputStream(new File("/Users/whm/Desktop/test")),null);
		
		String oldMd5 = Md5Utils.md5AsBase64(new File("/Users/whm/Desktop/test"));
		System.out.println("oldMd5:"+oldMd5);
		GetObjectResult object = client.getObject("murongyifei", "test1");
		String newMd5 = Md5Utils.md5AsBase64(object.getObject().getObjectContent());
		System.out.println("newMd5:"+newMd5);
	}
}
