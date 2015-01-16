package com.ksyun.ks3.service.object;

import java.io.File;

import org.junit.Test;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.GranteeEmail;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.service.ObjectBeforeTest;

public class PutObjectAclTest extends ObjectBeforeTest {
	
	
	@Test
	public void putObjectAcl001(){
		client.putObject(bucket, "pubObjectAcl001", new File("D:/objectTest/record.txt"));
		AccessControlList acl = new AccessControlList();
		acl.addGrant(new GranteeEmail("123@126.com"), Permission.Read);
		
		client.putObjectACL(bucket, "pubObjectAcl001", acl);
		
		System.out.println(client.getObjectACL(bucket, "pubObjectAcl001"));
	}
}
