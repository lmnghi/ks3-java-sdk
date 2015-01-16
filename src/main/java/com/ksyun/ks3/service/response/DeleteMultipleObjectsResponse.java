package com.ksyun.ks3.service.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.DeleteMultipleObjectsError;
import com.ksyun.ks3.dto.DeleteMultipleObjectsResult;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月10日 下午2:56:17
 * 
 * @description 
 **/
public class DeleteMultipleObjectsResponse extends Ks3WebServiceXmlResponse<DeleteMultipleObjectsResult>{
	private DeleteMultipleObjectsError error;
	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		
	}

	@Override
	public void startDocument() throws SAXException {
		result = new DeleteMultipleObjectsResult();
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("Error".equals(getTag()))
		{
			error = new DeleteMultipleObjectsError();
		}
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		if("Error".equals(getTag()))
		{
			this.result.addError(error);
		}
	}

	@Override
	public void string(String s) {
		if("Deleted".equals(getTag(1)))
		{
			if("Key".equals(getTag()))
				this.result.addDelete(s);
		}else if("Error".equals(getTag(1))){
			if("Key".equals(getTag()))
				this.error.setKey(s);
			else if("Code".equals(getTag()))
				this.error.setCode(s);
			else if("Message".equals(getTag()))
				this.error.setMessage(s);
		}
		
	}

}
