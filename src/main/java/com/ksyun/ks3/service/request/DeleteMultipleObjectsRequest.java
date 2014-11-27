package com.ksyun.ks3.service.request;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.service.request.support.MD5CalculateAble;
import com.ksyun.ks3.utils.StringUtils;
import com.ksyun.ks3.utils.XmlWriter;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月10日 下午2:44:52
 * 
 * @description 批量删除object的请求
 **/
public class DeleteMultipleObjectsRequest extends Ks3WebServiceRequest implements MD5CalculateAble{
	/**
	 * 要删除的object key
	 */
	private String[] keys = new String[]{};
	public DeleteMultipleObjectsRequest(String bucketName,List<String> keys)
	{
		this.setBucketname(bucketName);
		this.keys = keys.toArray(this.keys);
	}
	public DeleteMultipleObjectsRequest(String bucketName,String[] keys)
	{
		this.setBucketname(bucketName);
		this.keys = keys;
	}
	public String getMd5() {
		return com.ksyun.ks3.utils.Base64
				.encodeAsString(((MD5DigestCalculatingInputStream)super.getRequestBody())
						.getMd5Digest());
	}

	@Override
	protected void configHttpRequest() {
		this.addParams("delete","");
		this.setHttpMethod(HttpMethod.POST);
		XmlWriter writer = new XmlWriter();
		writer.start("Delete");
		for(int i =0;i<this.keys.length;i++)
		{
			writer.start("Object").start("Key").value(keys[i]).end().end();
		}
		writer.end();
		this.setRequestBody(new ByteArrayInputStream(writer.toString().getBytes()));
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(super.getBucketname()))
			throw new IllegalArgumentException("bucket name can not be null");
		if(this.keys == null)
			throw new IllegalArgumentException("the keys to delete can not be null");
	}
	public String[] getKeys() {
		return keys;
	}
	/**
	 * 设置要删除的object keys
	 * @param keys
	 */
	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	
}
