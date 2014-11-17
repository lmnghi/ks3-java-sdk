package com.ksyun.ks3.service.request;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;







import java.util.List;


import com.ksyun.ks3.RepeatableInputStream;
import com.ksyun.ks3.dto.ListPartsResult;
import com.ksyun.ks3.dto.Part;
import com.ksyun.ks3.dto.PartETag;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.XmlWriter;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 下午1:55:03
 * 
 * @description 完成分块上传
 **/
public class CompleteMultipartUploadRequest extends Ks3WebServiceRequest {
	/**
	 * 通过Init Multipart Upload 初始化得到的uploadId
	 */
	private String uploadId;
	/**
	 * 通过Upload Part返回的内容集合
	 */
	private List<PartETag> partETags = new ArrayList<PartETag>();
	public CompleteMultipartUploadRequest(String bucketname,String objectkey,String uploadId,List<PartETag> eTags)
	{
		this.setBucketname(bucketname);
		this.setObjectkey(objectkey);
		this.uploadId = uploadId;
		this.partETags = eTags;
	}
	public CompleteMultipartUploadRequest(ListPartsResult result)
	{
		this.setBucketname(result.getBucketname());
		this.setObjectkey(result.getKey());
		this.uploadId = result.getUploadId();
		for(Part p : result.getParts())
		{
			PartETag tag = new PartETag();
			tag.seteTag(p.getETag());
			tag.setPartNumber(p.getPartNumber());
		    this.partETags.add(tag);
		}
	}
	public CompleteMultipartUploadRequest(String bucketname,String objectkey)
	{
		super.setBucketname(bucketname);
		super.setObjectkey(objectkey);
	}
	public void addETag(PartETag eTag)
	{
		if(this.partETags==null)
		{
			partETags = new ArrayList<PartETag>();
		}
		this.partETags.add(eTag);
	}
	@Override
	protected void configHttpRequest() {
		XmlWriter writer = new XmlWriter();
		writer.start("CompleteMultipartUpload");
		for(PartETag tag:this.partETags)
		{
			writer.start("Part").start("PartNumber").value(tag.getPartNumber()).end().start("ETag").value(tag.geteTag()).end().end();
		}
		writer.end();
		String xml = writer.toString();
		this.setRequestBody(new ByteArrayInputStream(xml.getBytes()));
		this.setHttpMethod(HttpMethod.POST);
		this.addParams("uploadId", this.uploadId);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw new IllegalArgumentException("object key can not be null");
		if(StringUtils.isBlank(this.uploadId))
			throw new IllegalArgumentException("uploadId can not be null");
	}
	/**
	 * 通过Init Multipart Upload 初始化得到的uploadId
	 */
	public String getUploadId() {
		return uploadId;
	}
	/**
	 * 通过Init Multipart Upload 初始化得到的uploadId
	 */
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	/**
	 * 通过Upload Part返回的内容集合
	 */
	public List<PartETag> getPartETags() {
		return partETags;
	}
	/**
	 * 通过Upload Part返回的内容集合
	 */
	public void setPartETags(List<PartETag> partETags) {
		this.partETags = partETags;
	}
	
}
