package com.ksyun.ks3.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.Ks3ServiceException;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月15日 下午2:28:07
 * 
 * @description
 **/
public class XmlReader {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	private Document document = null;

	public XmlReader(String xml) {
		this(new ByteArrayInputStream(xml.getBytes()));
	}

	public XmlReader(InputStream is) {
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(is);
		} catch (Exception e) {
			throw new Ks3ClientException("解析xml文档出错(" + e
					+ ")", e);
		}
	}
	public Document getDocument()
	{
		return document;
	}
}
