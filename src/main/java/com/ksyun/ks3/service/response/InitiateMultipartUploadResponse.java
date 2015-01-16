package com.ksyun.ks3.service.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.InitiateMultipartUploadResult;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月23日 上午10:49:15
 * 
 * @description 
 **/
public class InitiateMultipartUploadResponse extends Ks3WebServiceXmlResponse<InitiateMultipartUploadResult>{

	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		
	}

	@Override
	public void startDocument() throws SAXException {
		result = new InitiateMultipartUploadResult();
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
		if("UploadId".equalsIgnoreCase(getTag()))
		{
			this.result.setUploadId(s);
		}
	}

}
