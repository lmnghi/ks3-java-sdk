package com.ksyun.ks3.demo;

import java.io.File;

import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.CreateBucketConfiguration;
import com.ksyun.ks3.dto.CreateBucketConfiguration.REGION;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月24日 上午10:48:14
 * 
 * @description demo
 **/
public class Demo {
	public static void main(String[] args){
		//初始化ks3 client
		String accessKeyId = "your accesskeyId";
		String accessKeySecret  ="your accesskeySecret";
		Ks3 client = new Ks3Client(accessKeyId,accessKeySecret);
		//通过ConfigLoader配置参数
		ClientConfig.addConfigLoader(new DemoConfigLoader());
		//一般的方式配置参数,配置最大重试次数
		ClientConfig.getConfig().set(ClientConfig.MAX_RETRY,"5");
		
		
		
		//列出当前用户下的bucket
		System.out.println(client.listBuckets());
		
		
		
		//新建一个bucket
		CreateBucketRequest request = new CreateBucketRequest("your bucket name");
		//设置要新建的bucket为私有的
		request.setCannedAcl(CannedAccessControlList.Private);
		//设置bucket的存储地点
		CreateBucketConfiguration config = new CreateBucketConfiguration(REGION.BEIJING);
		request.setConfig(config);
		//执行操作
		client.createBucket(request);
		
		
		
		//上传一个文件
		PutObjectRequest request1 = new PutObjectRequest("your bucket name","your object key",new File("your file path"));
		//设置为公开读
		request1.setCannedAcl(CannedAccessControlList.PublicRead);
		//设置元数据
		ObjectMetadata meta = new ObjectMetadata();
		//设置用户元数据时,如果不以x-kss-meta-开头，系统会在meta key前自动拼接x-kss-meta-
		meta.setUserMeta("your meta key", "your meta value");
		request1.setObjectMeta(meta);
		//执行操作
		client.putObject(request1);
		
		
		
		//初始化一个分块上传
		InitiateMultipartUploadRequest request2 = new InitiateMultipartUploadRequest("your bucket name","your object key");
		request2.setCannedAcl(CannedAccessControlList.PublicRead);
		//设置元数据
		ObjectMetadata meta1 = new ObjectMetadata();
		//设置用户元数据时,如果不以x-kss-meta-开头，系统会在meta key前自动拼接x-kss-meta-
		meta.setUserMeta("your meta key", "your meta value");
		request2.setObjectMeta(meta1);
		//执行操作
		InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(request2);
		
		
		
		//执行分块上传
		UploadPartRequest request3 = new UploadPartRequest(initResult.getBucket(),initResult.getKey(),initResult.getUploadId(),1,new File("your file path"),(long)(1024*5),0L);
		client.uploadPart(request3);
		//执行多次uploadPart
		
		
		
		//列出所有的part
		ListPartsResult listResult = client.listParts(initResult.getBucket(),initResult.getKey(),initResult.getUploadId());
		
		
		//完成上传
		client.completeMultipartUpload(listResult);
	}
}
