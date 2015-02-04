package com.ksyun.ks3.service;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.dto.Fop;
import com.ksyun.ks3.dto.FopInfo;
import com.ksyun.ks3.dto.FopTask;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.PostObjectFormFields;
import com.ksyun.ks3.exception.serviceside.QueryTaskFailException;
import com.ksyun.ks3.service.multipartpost.FormFieldKeyValuePair;
import com.ksyun.ks3.service.multipartpost.HttpPostEmulator;
import com.ksyun.ks3.service.multipartpost.UploadFileItem;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.PutPfopRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午6:18:56
 * 
 * @description 
 **/
public class PfopTest extends Ks3ClientTest{
	final String bucketName = "aaaaaaaavideo";
	final String key = "野生动物.3gp";
	final File file = new File("D://"+key);
	final File filePut = new File(this.getClass().getClassLoader().getResource("git.exe").toString().substring(6));
	final File logo = new File(this.getClass().getClassLoader().getResource("IMG.jpg").toString().substring(6));
	@Before
	public void createTestBucket(){
		if(!client.bucketExists(bucketName)){
			client.createBucket(bucketName);
		}else{
			client.clearBucket(bucketName);
		}
		try{
			client.headObject(bucketName, key);
		}catch(Exception e){
			client.putObject(bucketName,key,file);
		}
		client.putObject(bucketName, "IMG.jpg", logo);
	}
	@After
	public void deleteBucket(){
		if(client.bucketExists(bucketName)){
			client.clearBucket(bucketName);
			client.deleteBucket(bucketName);
		}
	}
	@Test(timeout=1000*2000)
	public void testPutPfop() throws InterruptedException{
		PutPfopRequest request = new PutPfopRequest(bucketName,key);
		List<Fop> fops = new ArrayList<Fop>();
		
		Fop fop1 = new Fop();
		fop1.setCommand("tag=avop&f=mp4&res=1280x720&vbr=1000k&abr=128k");
		fop1.setKey("野生动物-转码.3gp");
		fops.add(fop1);
		
		Fop fop2 = new Fop();
		fop2.setCommand("tag=avop&f=asf&clearmeta=1");
		fop2.setKey("野生动物-清除meta.3gp");
		fops.add(fop2);
		
		Fop fop3 = new Fop();
		fop3.setCommand("tag=avop&f=mp4&ss=5&t=15&vbr=1000k&abr=128k");
		fop3.setKey("野生动物-视频截取.3gp");
		fops.add(fop3);
		
		Fop fop4 = new Fop();
		fop4.setCommand("tag=avop&f=mp4&rotate=90&vbr=1000k&abr=128k&res=1280x720");
		fop4.setKey("野生动物-视频旋转.3gp");
		fops.add(fop4);
		
		Fop fop5 = new Fop();
		fop5.setCommand("tag=avop&f=mp4&vbr=1000k&abr=64k&text=5rWL6K+V5Lit5paH&textlayout=NORTHWEST&font=5b6u6L2v6ZuF6buR&fontcolor=Silver&fontsize=30&textwp=10&texthp=10");
		fop5.setKey("野生动物-文字水印.3gp");
		fops.add(fop5);
		
		Fop fop6 = new Fop();
		fop6.setCommand("tag=avop&f=mp4&vbr=1000k&abr=64k&imgwp=10&imghp=10&imglayout=SOURTHEAST&imgsrc=YWFhYWFhYWF2aWRlbzpJTUcuanBn");
		fop6.setKey("野生动物-图片水印.3gp");
		fops.add(fop6);
		
		Fop fop7 = new Fop();
		fop7.setCommand("tag=avop&f=mp4&an=1");
		fop7.setKey("野生动物-清除音频.3gp");
		fops.add(fop7);
		
		Fop fop8 = new Fop();
		fop8.setCommand("tag=avop&f=mp4&vn=1");
		fop8.setKey("野生动物-清除视频.3gp");
		fops.add(fop8);
		
		Fop fop9 = new Fop();
		fop9.setCommand("tag=avscrnshot&ss=10&res=640x360&rotate=90");
		fop9.setKey("野生动物-视频截图.jpg");
		fops.add(fop9);
		
		Fop fop10 = new Fop();
		fop10.setCommand("tag=avsample&ss=5&t=20&res=640x360&rotate=90&interval=5&pattern=6YeO55Sf5Yqo54mpLemHh+agt+e8qeeVpeWbvi0lM2QuanBn");
		fops.add(fop10);
		
		Fop fop11 = new Fop();
		fop11.setCommand("tag=avconcat&f=flv&mode=2&file=YWFhYWFhYWF2aWRlbzrph47nlJ/liqjniakuM2dw&loglevel=error");
		fop11.setKey("野生动物-视频拼接.3gp");
		fops.add(fop11);
		
		Fop fop12 = new Fop();
		fop12.setCommand("tag=avm3u8&segtime=10&abr=128k&vbr=1000k&res=1280x720");
		fop12.setKey("野生动物-hls切片.m3u8");
		fops.add(fop12);
		
		request.setFops(fops);
		request.setNotifyURL("http://10.4.2.38:19090/");
		String id = client.putPfopTask(request);
		FopTask task;
		while(true){
			task = client.getPfopTask(id);
			System.out.println(task);
			if(task.isProcessFinished()&&task.isNotified())
				break;
			Thread.sleep(5000);
		}
		assertEquals("3",task.getProcessstatus());
		assertEquals("1",task.getNotifystatus());
		for(FopInfo info :task.getFopInfos()){
			assertEquals(true,info.isSuccess());
		}
	}
	@Test(timeout=1000*600)
	public void testPutPfopWithErrorCommand() throws InterruptedException{
		PutPfopRequest request = new PutPfopRequest(bucketName,key);
		List<Fop> fops = new ArrayList<Fop>();
		
		Fop fop1 = new Fop();
		fop1.setCommand("tag=avop&f=mp41&res=1280x720&vbr=1000k&abr=128k");
		fop1.setKey("野生动物-转码.3gp");
		fops.add(fop1);
		
		request.setFops(fops);
		request.setNotifyURL("http://10.4.2.38:19090/");
		String id = client.putPfopTask(request);
		FopTask task;
		while(true){
			task = client.getPfopTask(id);
			System.out.println(task);
			if(task.isProcessFinished()&&task.isNotified())
				break;
			Thread.sleep(5000);
		}
		assertEquals("4",task.getProcessstatus());
		assertEquals("1",task.getNotifystatus());
		for(FopInfo info :task.getFopInfos()){
			assertEquals(false,info.isSuccess());
		}
	}
	@Test(expected=QueryTaskFailException.class)
	public void getPfopWithErrorId(){
		client.getPfopTask("notexist");
	}
	@Test(timeout=1000*600)
	public void putObjectWithPfop() throws InterruptedException{
		PutObjectRequest request = new PutObjectRequest(bucketName,key,file);
		
		
		List<Fop> fops = new ArrayList<Fop>();
		
		Fop fop1 = new Fop();
		fop1.setCommand("tag=avop&f=mp4&res=1280x720&vbr=1000k&abr=128k");
		fop1.setKey("野生动物-转码.3gp");
		fops.add(fop1);
		
		request.setFops(fops);
		request.setNotifyURL("http://10.4.2.38:19090/");
		
		
		String taskId = client.putObject(request).getTaskid();
		assertNotNull(taskId);
		FopTask task;
		while(true){
			task = client.getPfopTask(taskId);
			System.out.println(task);
			if(task.isProcessFinished()&&task.isNotified())
				break;
			Thread.sleep(5000);
		}
		assertEquals("3",task.getProcessstatus());
		assertEquals("1",task.getNotifystatus());
		for(FopInfo info :task.getFopInfos()){
			assertEquals(true,info.isSuccess());
		}
	}
	@Test(timeout=1000*600)
	public void mulitipartUploadWithPfop() throws InterruptedException{
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(bucketName,key);
		
		UploadPartRequest request = new UploadPartRequest(result.getBucket(),result.getKey(),result.getUploadId(),1,file,file.length(),0);
		client.uploadPart(request);	
		
		ListPartsResult parts = client.listParts(bucketName, result.getKey(), result.getUploadId());

		CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(parts);
		
		List<Fop> fops = new ArrayList<Fop>();
		
		Fop fop1 = new Fop();
		fop1.setCommand("tag=avop&f=mp4&res=1280x720&vbr=1000k&abr=128k");
		fop1.setKey("野生动物-转码.3gp");
		fops.add(fop1);
		
		compRequest.setFops(fops);
		compRequest.setNotifyURL("http://10.4.2.38:19090/");
		
		String taskId = client.completeMultipartUpload(compRequest).getTaskid();
		
		assertNotNull(taskId);
		FopTask task;
		while(true){
			task = client.getPfopTask(taskId);
			System.out.println(task);
			if(task.isProcessFinished()&&task.isNotified())
				break;
			Thread.sleep(5000);
		}
		assertEquals("3",task.getProcessstatus());
		assertEquals("1",task.getNotifystatus());
		for(FopInfo info :task.getFopInfos()){
			assertEquals(true,info.isSuccess());
		}
	}
	@Test
	public void postObjectPfop() throws Exception{
		Map<String,String> postData = new HashMap<String,String>();

		postData.put("key",key);
		
		PostObjectFormFields fields = client.postObject(bucketName,key, postData,null);
		
		postData.put("policy",fields.getPolicy());
		postData.put("KSSAccessKeyId",fields.getKssAccessKeyId());
		postData.put("signature",fields.getSignature());
		
		String serverUrl = "http://kss.ksyun.com/"+bucketName;//上传地址  
          
	    // 设定要上传的普通Form Field及其对应的value  
	    ArrayList<FormFieldKeyValuePair> ffkvp = new ArrayList<FormFieldKeyValuePair>();  
	    
	    for(Entry<String,String> entry:postData.entrySet()){
	    	ffkvp.add(new FormFieldKeyValuePair(entry.getKey(),entry.getValue()));
	    }
	  
	    // 设定要上传的文件  
	    ArrayList<UploadFileItem> ufi = new ArrayList<UploadFileItem>();  
	    ufi.add(new UploadFileItem("file","D://"+key)); 
	        
	    Map<String,String> headers = new HashMap<String,String>();
	    headers.put("fops",URLEncoder.encode("tag=avop&f=mp4&res=1280x720&vbr=1000k&abr=128k"));
	    headers.put("notifyURL","http://10.4.2.38:19090/");
	    HttpPostEmulator hpe = new HttpPostEmulator();  
	    Map<String, List<String>> response = hpe.sendHttpPostRequest(serverUrl, ffkvp, ufi,headers);  
	    System.out.println("Responsefrom server is: " + response);   
	    List<String> taskid = response.get("taskid");
	    assertNotNull(taskid);
	    assertEquals(1,taskid.size());
	    FopTask task;
		while(true){
			task = client.getPfopTask(taskid.get(0));
			System.out.println(task);
			if(task.isProcessFinished()&&task.isNotified())
				break;
			Thread.sleep(5000);
		}
		assertEquals("3",task.getProcessstatus());
		assertEquals("1",task.getNotifystatus());
		for(FopInfo info :task.getFopInfos()){
			assertEquals(true,info.isSuccess());
		}
	}
}
