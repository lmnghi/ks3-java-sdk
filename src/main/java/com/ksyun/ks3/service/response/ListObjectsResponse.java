package com.ksyun.ks3.service.response;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.Ks3ObjectSummary;
import com.ksyun.ks3.dto.ObjectListing;
import com.ksyun.ks3.dto.Owner;
import com.ksyun.ks3.utils.DateUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月16日 下午3:39:35
 * 
 * @description
 **/
public class ListObjectsResponse extends
		Ks3WebServiceXmlResponse<ObjectListing> {

	private List<Ks3ObjectSummary> objs = null;
	private Ks3ObjectSummary obj = null;
	private Owner owner = null;
	private List<String> prefixs = new ArrayList<String>();

	@Override
	public void startDocument() throws SAXException {
		result = new ObjectListing();
		objs = new ArrayList<Ks3ObjectSummary>();
		result.setCommonPrefixes(prefixs);
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		String tag = getTag();
		if ("Contents".equals(tag)) {
			obj = new Ks3ObjectSummary();
		} else if ("Owner".equals(tag)) {
			owner = new Owner();
		}
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		String tag = getTag();
		if ("Owner".equals(tag)) {
			obj.setOwner(owner);
		} else if ("Contents".equals(tag)) {
			obj.setBucketName(result.getBucketName());
			objs.add(obj);
		} else if ("ListBucketResult".equals(tag)) {
			result.setObjectSummaries(objs);
		}
	}

	@Override
	public void string(String s) {
		String tag = getTag();
		if ("CommonPrefixes".equals(getTag(1))) {
			if ("Prefix".equals(tag)) {
				this.prefixs.add(s);
			}
		} else {
			if ("Name".equals(tag)) {
				result.setBucketName(s);
			} else if ("Prefix".equals(tag)) {
				result.setPrefix(s);
			} else if ("Marker".equals(tag)) {
				result.setMarker(s);
			} else if ("MaxKeys".equals(tag)) {
				result.setMaxKeys(Integer.parseInt(s));
			} else if ("Delimiter".equals(tag)) {
				result.setDelimiter(s);
			} else if ("IsTruncated".equals(tag)) {
				result.setTruncated("true".equals(s));
			} else if ("Key".equals(tag)) {
				obj.setKey(s);
			} else if ("LastModified".equals(tag)) {
				obj.setLastModified(DateUtils.convertStr2Date(s));
			} else if ("ETag".equals(tag)) {
				obj.setETag(s);
			} else if ("Size".equals(tag)) {
				obj.setSize(Long.parseLong(s));
			} else if ("ID".equals(tag)) {
				owner.setId(s);
			} else if ("DisplayName".equals(tag)) {
				owner.setDisplayName(s);
			} else if ("StorageClass".equals(tag)) {
				obj.setStorageClass(s);
			} else if ("Prefix".equals(tag)) {
				prefixs.add(s);
			} else if ("NextMarker".equals(tag)) {
				result.setNextMarker(s);
			} else if("Encoding-Type".equals(tag)){
				result.setEncodingType(s);
			}
		}
	}

	@Override
	public void preHandle() {

	}

	public int[] expectedStatus() {
		return new int[] { 200 };
	}
}
