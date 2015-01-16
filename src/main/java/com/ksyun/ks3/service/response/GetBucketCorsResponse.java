package com.ksyun.ks3.service.response;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.BucketCorsConfiguration;
import com.ksyun.ks3.dto.CorsRule;
import com.ksyun.ks3.dto.CorsRule.AllowedMethods;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月31日 下午1:52:17
 * 
 * @description 
 **/
public class GetBucketCorsResponse extends Ks3WebServiceXmlResponse<BucketCorsConfiguration>{
	private CorsRule rule;
	private List<AllowedMethods> allowedMethods;
	private List<String> allowedOrigins;
	private List<String> exposedHeaders;
	private List<String> allowedHeaders;
	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		result = new BucketCorsConfiguration();
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("CORSRule".equals(getTag())){
			rule = new CorsRule();
			allowedMethods = new ArrayList<AllowedMethods>();
			allowedOrigins = new ArrayList<String>();
			exposedHeaders = new ArrayList<String>();
			allowedHeaders = new ArrayList<String>();
		}
		
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		if("CORSRule".equals(getTag())){
			rule.setAllowedHeaders(allowedHeaders);
			rule.setAllowedMethods(allowedMethods);
			rule.setAllowedOrigins(allowedOrigins);
			rule.setExposedHeaders(exposedHeaders);
			result.addRule(rule);
		}
	}

	@Override
	public void string(String s) {
		if("MaxAgeSeconds".equals(getTag())){
			rule.setMaxAgeSeconds(Integer.parseInt(s));
		}else if("AllowedHeader".equals(getTag())){
			allowedHeaders.add(s);
		}else if("AllowedMethod".equals(getTag())){
			allowedMethods.add(AllowedMethods.load(s));
		}else if("AllowedOrigin".equals(getTag())){
			allowedOrigins.add(s);
		}else if("ExposeHeader".equals(getTag())){
			exposedHeaders.add(s);
		}
	}

}
