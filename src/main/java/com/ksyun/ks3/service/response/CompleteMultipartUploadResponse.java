package com.ksyun.ks3.service.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.CompleteMultipartUploadResult;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月23日 下午2:06:10
 * 
 * @description 
 **/
public class CompleteMultipartUploadResponse extends
		Ks3WebServiceXmlResponse<CompleteMultipartUploadResult> {

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		
	}

	@Override
	public void startDocument() throws SAXException {
		result = new CompleteMultipartUploadResult();
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {	
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
	}

	@Override
	public void string(String s) {
		if("Location".equalsIgnoreCase(getTag()))
		{
			result.setLocation(s);
		}else if("Bucket".equalsIgnoreCase(getTag()))
		{
			result.setBucket(s);
		}else if("Key".equalsIgnoreCase(getTag()))
		{
			result.setKey(s);
		}else if("ETag".equalsIgnoreCase(getTag()))
		{
			result.seteTag(s);
		}
	}

}
