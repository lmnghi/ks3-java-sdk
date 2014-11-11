package com.ksyun.ks3.service;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.ksyun.ks3.dto.Bucket;
import com.ksyun.ks3.dto.CompleteMultipartUploadResult;
import com.ksyun.ks3.dto.HeadObjectResult;
import com.ksyun.ks3.dto.InitiateMultipartUploadResult;
import com.ksyun.ks3.dto.Ks3Object;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.dto.*;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;
import com.ksyun.ks3.service.request.*;
import com.ksyun.ks3.service.response.GetBucketACLResponse;
import com.ksyun.ks3.service.request.AbortMultipartUploadRequest;
import com.ksyun.ks3.service.request.CompleteMultipartUploadRequest;
import com.ksyun.ks3.service.request.CreateBucketRequest;
import com.ksyun.ks3.service.request.DeleteBucketRequest;
import com.ksyun.ks3.service.request.DeleteObjectRequest;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.HeadBucketRequest;
import com.ksyun.ks3.service.request.HeadObjectRequest;
import com.ksyun.ks3.service.request.InitiateMultipartUploadRequest;
import com.ksyun.ks3.service.request.ListBucketsRequest;
import com.ksyun.ks3.service.request.ListObjectsRequest;
import com.ksyun.ks3.service.request.ListPartsRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.UploadPartRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午5:30:30
 * 
 * @description ks3客户端，用户使用时需要先配置ClientConfig{@link ClientConfig}
 *              ,然后初始化一个Ks3Client进行操作
 **/
public interface Ks3 {
	/**
	 * GET SERVICE
	 * 
	 * @return {@link Bucket}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出当前用户下的所有bucket
	 *             </p>
	 */
	public List<Bucket> listBuckets() throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * GET SERVICE
	 * 
	 * @param request
	 *            {@link ListBucketsRequest}
	 * @return {@link Bucket}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出当前用户下的所有bucket
	 *             </p>
	 */
	public List<Bucket> listBuckets(ListBucketsRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET BUCKET acl
	 * 
	 * @param bucketName
	 *            bucket名称
	 * @return {@link AccessControlPolicy}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取bucket的acl
	 *             </p>
	 */
	public AccessControlPolicy getBucketACL(String bucketName)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET Bucket acl
	 * 
	 * @param request
	 *            {@link GetBucketACLRequest}
	 * @return {@link AccessControlPolicy}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取bucket的acl
	 *             </p>
	 */
	public AccessControlPolicy getBucketACL(GetBucketACLRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * PUT Bucket acl
	 * 
	 * @param bucketName
	 *            bucket名称
	 * @param accessControlList
	 *            {@link AccessControlList}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             编辑bucket的acl
	 *             </p>
	 */
	public void putBucketACL(String bucketName,
			AccessControlList accessControlList) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * PUT Bucket acl
	 * 
	 * @param bucketName
	 *            bucket名称
	 * @param CannedAcl
	 *            {@link CannedAccessControlList}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             编辑bucket的acl
	 *             </p>
	 */
	public void putBucketACL(String bucketName,
			CannedAccessControlList CannedAcl) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * PUT Bucket acl
	 * 
	 * @param request
	 *            {@link PutBucketACLRequest}
	 * @return
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             编辑bucket的acl
	 *             </p>
	 */
	public void putBucketACL(PutBucketACLRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * PUT OBJECT ACL
	 * 
	 * @param bucketName
	 *            bucket名称
	 * @param objectName
	 *            object名称
	 * @param accessControlList
	 *            {@link CannedAccessControlList}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             编辑object的acl
	 *             </p>
	 */
	public void putObjectACL(String bucketName, String objectName,
			CannedAccessControlList accessControlList)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * PUT OBJECT ACL
	 * 
	 * @param bucketName
	 *            bucket名称
	 * @param objectName
	 *            object名称
	 * @param accessControlList
	 *            {@link AccessControlList}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             编辑object的acl
	 *             </p>
	 */
	public void putObjectACL(String bucketName, String objectName,
			AccessControlList accessControlList) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * PUT Object acl
	 * 
	 * @param request
	 *            {@link PutObjectACLRequest}
	 * @return
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             编辑object的acl
	 *             </p>
	 */
	public void putObjectACL(PutObjectACLRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * Get Object acl
	 * 
	 * @param bucketName
	 *            bucket名称
	 * @param ObjectName
	 *            object名称
	 * @return {@link AccessControlPolicy}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取object的acl
	 *             </p>
	 */
	public AccessControlPolicy getObjectACL(String bucketName, String ObjectName)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * Get Object acl
	 * 
	 * @param request
	 *            {@link GetObjectACLRequest}
	 * @return {@link AccessControlPolicy}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取object的acl
	 *             </p>
	 */
	public AccessControlPolicy getObjectACL(GetObjectACLRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * HEAD BUECKET
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @return statue code and headers{@link HeadBucketResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             返回headers
	 *             </p>
	 */
	public HeadBucketResult headBucket(String bucketname)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * HEAD BUCKET
	 * 
	 * @param request
	 *            {@link HeadBucketRequest}
	 * @return statue code and headers{@link HeadBucketResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             返回headers
	 *             </p>
	 */
	public HeadBucketResult headBucket(HeadBucketRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @return 如果bucket存在则为true
	 * @throws Ks3ClientException
	 *             <p>
	 *             判断bucket是否存在,bucket存在但不属于当前用户也会返回true
	 *             </p>
	 */
	public boolean bucketExists(String bucketname) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * PUT BUCKET
	 * 
	 * @param bucketname
	 *            bucket名称
	 *            <p>
	 *            Bucket是存放Object的容器，所有的Object都必须存放在特定的Bucket中。
	 *            每个用户最多可以创建20个Bucket
	 *            ，每个Bucket中可以存放无限多个Object。Bucket不能嵌套，每个Bucket中只能存放Object，
	 *            不能再存放Bucket ，Bucket下的Object是一个平级的结构。
	 *            <p>
	 *            <p>
	 *            Bucket的名称全局唯一且命名规则与DNS命名规则相同：
	 *            <p>
	 *            <ul>
	 *            <li>长度3-63，</li>
	 *            <li>不包含大写字母，不包含[‘ ’,\t,\r,\n]，不包含连续的’.’</li>
	 *            <li>，’.’和’-’在bucket名称中不能相连，</li>
	 *            <li>仅可包含. - 数字 小写字母，</li>
	 *            <li>不以’.’或’-’结尾</li>
	 *            </ul>
	 * @return {@link Bucket}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             创建bucket
	 *             </p>
	 */
	public Bucket createBucket(String bucketname) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * PUT BUCKET
	 * 
	 * @param request
	 *            {@link CreateBucketRequest}
	 * @return {@link Bucket}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             创建bucket
	 *             </p>
	 */
	public Bucket createBucket(CreateBucketRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * DELETE BUCKET
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除bucket，bucket中内容为空时可以删除成功
	 *             </p>
	 */
	public void deleteBucket(String bucketname) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * DELETE BUCKET
	 * 
	 * @param request
	 *            {@link DeleteBucketRequest}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除bucket，bucket中内容为空时可以删除成功
	 *             </p>
	 */
	public void deleteBucket(DeleteBucketRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET BUCKET(LIST OBJECTS)
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @return {@link ObjectListing}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出bucket下满足条件的object
	 *             </p>
	 */
	public ObjectListing listObjects(String bucketname)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET BUCKET(LIST OBJECTS)
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param prefix
	 *            前缀
	 * @return {@link ObjectListing}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出bucket下满足条件的object
	 *             </p>
	 */
	public ObjectListing listObjects(String bucketname, String prefix)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET BUCKET(LIST OBJECTS)
	 * 
	 * @param request
	 *            {@link ListObjectsRequest}
	 * @return {@link ObjectListing}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出bucket下满足条件的object
	 *             </p>
	 */
	public ObjectListing listObjects(ListObjectsRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * DELETE OBJECT
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param key
	 *            object的key(即名称)
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除指定的object
	 *             </p>
	 */
	public void deleteObject(String bucketname, String key)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * DELETE OBJECT
	 * 
	 * @param request
	 *            {@link DeleteObjectRequest}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除指定的object
	 *             </p>
	 */
	public void deleteObject(DeleteObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * DELETE Multiple Objects
	 * 
	 * @param request
	 *            {@link DeleteMultipleObjectsRequest}
	 * @return {@link DeleteMultipleObjectsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除若干的objects
	 *             </p>
	 */
	public DeleteMultipleObjectsResult deleteObjects(
			DeleteMultipleObjectsRequest request) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * DELETE Multiple Objects
	 * 
	 * @param keys
	 *            要删除的keys
	 * @return {@link DeleteMultipleObjectsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除若干的objects
	 *             </p>
	 */
	public DeleteMultipleObjectsResult deleteObjects(List<String> keys,
			String bucketName) throws Ks3ClientException, Ks3ServiceException;

	/**
	 * DELETE Multiple Objects
	 * 
	 * @param keys
	 *            要删除的keys
	 * @return {@link DeleteMultipleObjectsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             删除若干的objects
	 *             </p>
	 */
	public DeleteMultipleObjectsResult deleteObjects(String[] keys,
			String bucketName) throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET OBJECT
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param key
	 *            object的key(即名称)
	 * @return {@link GetObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取指定的object
	 *             </p>
	 */
	public GetObjectResult getObject(String bucketname, String key)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * GET OBJECT
	 * 
	 * @param request
	 *            {@link GetObjectRequest}
	 * @return {@link GetObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取指定的object
	 *             </p>
	 */
	public GetObjectResult getObject(GetObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * PUT OBJECT
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即名称,编码后的长度不得超过1024个字节。)
	 * @param file
	 *            要上传的文件
	 * @return {@link PutObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             上传object
	 *             </p>
	 */
	public PutObjectResult PutObject(String bucketname, String objectkey,
			File file) throws Ks3ClientException, Ks3ServiceException;

	/**
	 * PUT OBJECT
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即名称,编码后的长度不得超过1024个字节。)
	 * @param inputstream
	 *            数据流
	 * @param objectmeta
	 *            object元数据{@link ObjectMetadata}
	 * @return {@link PutObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             上传object
	 *             </p>
	 */
	public PutObjectResult PutObject(String bucketname, String objectkey,
			InputStream inputstream, ObjectMetadata objectmeta)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * PUT OBJECT
	 * 
	 * @param request
	 *            {@link PutObjectRequest}
	 * @return {@link PutObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             上传object
	 *             </p>
	 */
	public PutObjectResult putObject(PutObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * HEAD OBJECT
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即object名称)
	 * @return {@link HeadObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取object的元数据、etag、上次修改时间
	 *             </p>
	 */
	public HeadObjectResult headObject(String bucketname, String objectkey)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * HEAD OBJECT
	 * 
	 * @param request
	 *            {@link HeadObjectRequest}
	 * @return {@link HeadObjectResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             获取object的元数据、etag、上次修改时间
	 *             </p>
	 */
	public HeadObjectResult headObject(HeadObjectRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * Initiate Multipart Upload
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即object名称)
	 * @return {@link InitiateMultipartUploadResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             初始化分块上传
	 *             </p>
	 */
	public InitiateMultipartUploadResult initiateMultipartUpload(
			String bucketname, String objectkey) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * Initiate Multipart Upload
	 * 
	 * @param request
	 *            {@link InitiateMultipartUploadRequest}
	 * @return {@link InitiateMultipartUploadResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             初始化分块上传
	 *             </p>
	 */
	public InitiateMultipartUploadResult initiateMultipartUpload(
			InitiateMultipartUploadRequest request) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * Upload Part
	 * 
	 * @param request
	 *            {@link UploadPartRequest}
	 * @return {@link PartETag}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             分块上传文件
	 *             </p>
	 */
	public PartETag uploadPart(UploadPartRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * Complete Multipart Upload
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即名称)
	 * @param uploadId
	 *            通过初始化分块上传获取到的uploadId
	 * @param partETags
	 *            <p>
	 *            public PartETag uploadPart(UploadPartRequest request)throws
	 *            Ks3ClientException, Ks3ServiceException;
	 *            <p>
	 *            获取到的结果{@link PartETag}
	 * @return {@link CompleteMultipartUploadResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             完成分块上传，使ks3服务器将之前上传的小块合并成一个object
	 *             </p>
	 */
	public CompleteMultipartUploadResult completeMultipartUpload(
			String bucketname, String objectkey, String uploadId,
			List<PartETag> partETags) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * Complete Multipart Upload
	 * 
	 * @param request
	 *            {@link CompleteMultipartUploadRequest}
	 * @return {@link CompleteMultipartUploadResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             完成分块上传，使ks3服务器将之前上传的小块合并成一个object
	 *             </p>
	 */
	public CompleteMultipartUploadResult completeMultipartUpload(
			CompleteMultipartUploadRequest request) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * Abort Multipart Upload
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即object的名称)
	 * @param uploadId
	 *            通过初始化分块上传获取到的uploadId
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             中止分块上传
	 *             </p>
	 */
	public void abortMultipartUpload(String bucketname, String objectkey,
			String uploadId) throws Ks3ClientException, Ks3ServiceException;

	/**
	 * Abort Multipart Upload
	 * 
	 * @param request
	 *            {@link AbortMultipartUploadRequest}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             中止分块上传
	 *             </p>
	 */
	public void abortMultipartUpload(AbortMultipartUploadRequest request)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * List Parts
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即object的名称)
	 * @param uploadId
	 *            通过初始化分块上传获取到的uploadId
	 * @return {@link ListPartsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出该uploadid下已经上传成功的块
	 *             </p>
	 */
	public ListPartsResult ListParts(String bucketname, String objectkey,
			String uploadId) throws Ks3ClientException, Ks3ServiceException;

	/**
	 * List Parts
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即object的名称)
	 * @param uploadId
	 *            通过初始化分块上传获取到的uploadId
	 * @param maxParts
	 *            列出的最大结果数
	 * @return {@link ListPartsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出该uploadid下已经上传成功的块
	 *             </p>
	 */
	public ListPartsResult ListParts(String bucketname, String objectkey,
			String uploadId, int maxParts) throws Ks3ClientException,
			Ks3ServiceException;

	/**
	 * List Parts
	 * 
	 * @param bucketname
	 *            bucket名称
	 * @param objectkey
	 *            object的key(即object的名称)
	 * @param uploadId
	 *            通过初始化分块上传获取到的uploadId
	 * @param maxParts
	 *            列出的最大结果数
	 * @param partNumberMarker
	 *            partnumber游标，即从第partNumberMarker开始罗列
	 * @return {@link ListPartsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出该uploadid下已经上传成功的块
	 *             </p>
	 */
	public ListPartsResult ListParts(String bucketname, String objectkey,
			String uploadId, int maxParts, int partNumberMarker)
			throws Ks3ClientException, Ks3ServiceException;

	/**
	 * List Parts
	 * 
	 * @param request
	 *            {@link ListPartsRequest}
	 * @return {@link ListPartsResult}
	 * @throws Ks3ClientException
	 * @throws Ks3ServiceException
	 *             <p>
	 *             列出该uploadid下已经上传成功的块
	 *             </p>
	 */
	public ListPartsResult ListParts(ListPartsRequest request)
			throws Ks3ClientException, Ks3ServiceException;
}
