package com.ksyun.ks3;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ksyun.ks3.dto.*;

import org.junit.Before;
import org.junit.Test;

import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.AccessControlPolicy;
import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.CompleteMultipartUploadResult;
import com.ksyun.ks3.dto.CreateBucketConfiguration.REGION;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.Grantee;
import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.Ks3Object;
import com.ksyun.ks3.dto.Ks3ObjectSummary;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.http.Ks3CoreController;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.ListObjectsRequest;
import com.ksyun.ks3.service.request.ListPartsRequest;
import com.ksyun.ks3.service.request.PutBucketACLRequest;
import com.ksyun.ks3.service.request.PutObjectACLRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;
import com.ksyun.ks3.service.response.CompleteMultipartUploadResponse;
import com.ksyun.ks3.service.response.HeadObjectResponse;
import com.ksyun.ks3.utils.Timer;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月15日 上午10:28:53
 * 
 * @description
 **/
public class Ks3ClientTest {
	private Ks3Client client = new Ks3Client("2HITWMQXL2VBB3XMAEHQ","ilZQ9p/NHAK1dOYA/dTKKeIqT/t67rO6V2PrXUNr");

	@Before
	public void init() {
		ClientConfig config = ClientConfig.getConfig();
	}

	 //@Test
	public void ListBuckets() {
		List<Bucket> buckets = client.listBuckets();
		System.out.println(buckets);
	}

    //@Test
	public void ListObjects() {
    	
/*		ObjectListing o = client.listObjects("yyy");
		Object od = o;
		System.out.println(od);*/
    	ListObjectsRequest request = new ListObjectsRequest("lijunwei.test",null,null,null,0);
    	ObjectListing o = client.listObjects(request);
    	Object od = o;
		System.out.println(od);
	}

	 @Test
	public void createAndDeleteBucket() {
		CreateBucketRequest request = new CreateBucketRequest("lijunwei",REGION.BEIJING); 
		client.createBucket(request);
		client.deleteBucket("lijunwei");
	}

	 @Test
	public void getObject() throws IOException {
		GetObjectResult obj = client.getObject("lijunwei.test", "test1/1234.bmp");
		System.out.println(obj);
		try {
			OutputStream os = new FileOutputStream(new File("D://"
					+ obj.getObject().getKey()));
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			InputStream in=obj.getObject().getObjectContent();
			while ((bytesRead = in.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			obj.getObject().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    //@Test
	public void deleteObject() {
    	ListObjectsRequest request = new ListObjectsRequest("ksc-scm","square/",null,null,null);
    	ObjectListing o = client.listObjects(request);
    	for(Ks3ObjectSummary sum:o.getObjectSummaries())
    	{
		     client.deleteObject("ksc-scm", sum.getKey());
    	}
    	for(String s :o.getCommonPrefixes())
    	{
    		client.deleteObject("ksc-scm", s);
    	}
	}
	 //@Test
	public void putObject() {

/*		PutObjectRequest request = new PutObjectRequest("lijunwei.test",
				"scrt712-x86.exe", new File("C:\\Users\\lijunwei\\Downloads\\scrt712-x86.exe"));
		request.getObjectMeta().addOrEditUserMeta("x-kss-meta-lijunwei",
				"lijunwei");
		request.getObjectMeta().addOrEditMeta(Meta.CacheControl,
				"only-if-cached");
		client.putObject(request);*/
		
/*		  request.setCannedAcl(CannedAccessControlList.PublicRead);
		  AccessControlList acl = new AccessControlList(); HashSet<Grant>
		  grants = new HashSet<Grant>(); GranteeId g1 = new GranteeId();
		  g1.setIdentifier("1234");
		  g1.setDisplayName("123"); Grant gt1 = new
		  Grant(g1,Permission.FullControl); grants.add(gt1); GranteeId g2 = new
				  GranteeId(); g2.setIdentifier("aaaa");g2.setDisplayName("aaa"); Grant gt2 = new
		  Grant(g2,Permission.Read); grants.add(gt2); acl.setGrants(grants);
		request.setAcl(acl);
		  
		   client.putObject(request); */
/*		   ObjectMetadata meta = new ObjectMetadata();
		  meta.addOrEditUserMeta(ObjectMetadata.userMetaPrefix + "test",
		  "123"); // meta.setUserMetadata(userMetadata); try {
		  client.PutObject("lijunwei.test", "IMG_16721.jpg", new
		  FileInputStream(new File("D://IMG_16721.jpg")), meta); } catch
		  (Ks3ServiceException e) { // TODO Auto-generated catch block
		  e.printStackTrace(); } catch (Ks3ClientException e) { // TODO
		  Auto-generated catch block e.printStackTrace(); } catch
		  (FileNotFoundException e) { // TODO Auto-generated catch block
		  e.printStackTrace(); }*/
		try {
			PutObjectRequest request = new PutObjectRequest("daiwenjun",
					"IMG_16721。exe",new File("D://新建文件夹.rar"));
			client.putObject(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		GetObjectRequest request = new GetObjectRequest("lijunwei.test", "IMG_16721。exe");
		request.setRange(0,499);
		request.setModifiedSinceConstraint(new Date());
		//List<String> aa = new ArrayList<String>();
		//aa.add("73a2ec41f952b0d62e1dacff25889d5a");
	//	request.setNonmatchingEtagConstraints(aa);
		GetObjectResult obj = client.getObject(request);
		System.out.println(obj);*/
/*			try {
				PutObjectRequest request = new PutObjectRequest("lijunwei.test",
						"%2f",new ByteArrayInputStream(new byte[]{}),new ObjectMetadata());
				client.putObject(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		 
	}

	// //@Test
	public void headObject() {
		HeadObjectResult response = client.headObject("lijunwei.test",
				"IMG_16721.jpg");
		System.out.println("");
	}

	// //@Test
	public void initMultipart() {
		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(
				"lijunwei.test", "eclipse.zip");
		request.setCannedAcl(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client
				.initiateMultipartUpload(request);
		System.out.println(result);

	}

	//@Test
	public void uploadPart() {
		long part = 5*1024*1024;
		String bucket = "lijunwei.test";
		String key = "IMG_16721.jpg";
		//String filename = "D://新建文件夹.rar";
		String filename = "D://IMG_16721.jpg";
		
		
		InitiateMultipartUploadRequest request1 = new InitiateMultipartUploadRequest(
				bucket, key);
		request1.setCannedAcl(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult result = client.initiateMultipartUpload(request1);
		System.out.println(result);
		//upload
		File file = new File(filename);
		long n = file.length() / part;
		System.out.println(n);
		for (int i = 0; i <= n; i++) {
			UploadPartRequest request = new UploadPartRequest(result.getBucket(),
					result.getKey(), result.getUploadId(), i+1, file,
					part, (long)i * part);
			PartETag tag = client.uploadPart(request);
			System.out.println(String.valueOf(i+1)+"  "+tag+"\n");
			try {
				UploadPartTime.print(i+1,Timer.end());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//list parts
		ListPartsRequest requestList = new ListPartsRequest(result.getBucket(),result.getKey(),result.getUploadId());
		ListPartsResult tags = client.ListParts(requestList);
		//complete
		CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(tags);
		client.completeMultipartUpload(request);
	}

	// //@Test
	public void completeMulti() {
		CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(
				"lijunwei.test", "eclipse.zip",
				"aedf953924f34d0ba93b74188de1596b", null);
		request.addETag(new PartETag(1, "d41d8cd98f00b204e9800998ecf8427e"));
		request.addETag(new PartETag(2, "d41d8cd98f00b204e9800998ecf8427e"));
		request.addETag(new PartETag(3, "d41d8cd98f00b204e9800998ecf8427e"));
		CompleteMultipartUploadResult response = client
				.completeMultipartUpload(request);
		System.out.println(response);
	}

	// //@Test
	public void listParts() {
		ListPartsResult result = client.ListParts("lijunwei.test",
				"eclipse.zip", "aedf953924f34d0ba93b74188de1596b");
		System.out.println(result);
	}

	 //@Test
	public void abortMulti() {
		client.abortMultipartUpload("lijunwei.test", "bigFile.rar",
				"44157d71c6e741699c8c5fb1f4f61aff");
	}
    //@Test
    public void getBucketACL(){
        AccessControlPolicy getBucketACL = client.getBucketACL("ksc-scm");
        System.out.println(getBucketACL.getAccessControlList());
    }
    //@Test
    public void putBucketACL(){
    	PutBucketACLRequest request = new PutBucketACLRequest("ksc-scm");
    	AccessControlList acl = new AccessControlList();
    	acl.addGrant(GranteeUri.AllUsers, Permission.Read);
    //	request.setCannedAcl(CannedAccessControlList.Private);
    	request.setAccessControlList(acl);
    	client.putBucketACL(request);
    }
    //@Test
    public void putObjectACL(){
    	PutObjectACLRequest request = new PutObjectACLRequest("ksc-scm","这个事测试.doc");
    	AccessControlList acl = new AccessControlList();
/*    	Grantee grantee = new GranteeId();
    	grantee.setIdentifier("1E74015858B022A60108039F");*/
    	acl.addGrant(GranteeUri.AllUsers, Permission.Read);
    	request.setAccessControlList(acl);
    	//request.setCannedAcl(CannedAccessControlList.Private);
    	client.putObjectACL(request);
    }
   //@Test
    public void getObjectACL(){
	   AccessControlPolicy getObjectACL = client.getObjectACL("ksc-scm","这个事测试.doc");
        System.out.println(getObjectACL.getAccessControlList());
    }
  //  //@Test
    public void configBucketAcl()
    {
    	PutBucketACLRequest request = new PutBucketACLRequest("ksc-scm",CannedAccessControlList.PublicReadWrite);
    	client.putBucketACL(request);
    }
    //@Test
    public void deleteObjects()
    {
    	System.out.println(client.deleteObjects(new String[]{"11112018rln5.pdf","dfdfdsf.pdf","sssss","square/"}, "ksc-scm"));
    }
    static int i = 0;
    @Test
    public void test()
    {
    	
    	for(;;i++){
    		partDownLoad();
    	}
    }
    public void partDownLoad()
    {
    	GetObjectRequest request = new GetObjectRequest("lijunwei.test","1234.jpeg");
    	long max = 1024*1024;
    	long index = 0;
    	long step = 1024*1024*1024;
    	for(;index<=max;index=index+step+1){
    		request.setRange(index,index+step);
    		GetObjectResult result = client.getObject(request);
    		max = result.getObject().getObjectMetadata().getInstanceLength();
    		
    		try {
    			OutputStream os = new FileOutputStream(new File("D://ggg/"+i+"--"
    					+ result.getObject().getKey()),true);
    			
    			int bytesRead = 0;
    			byte[] buffer = new byte[ 8192];
    			while ((bytesRead = result.getObject().getObjectContent().read(buffer, 0, 8192)) != -1) {
    				os.write(buffer, 0, bytesRead);
    			}
    			os.close();
    			result.getObject().close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }
}
