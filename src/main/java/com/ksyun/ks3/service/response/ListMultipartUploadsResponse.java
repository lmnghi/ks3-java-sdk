package com.ksyun.ks3.service.response;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.ListMultipartUploadsResult;
import com.ksyun.ks3.dto.MultiPartUploadInfo;
import com.ksyun.ks3.dto.Owner;
import com.ksyun.ks3.utils.DateUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description
 **/
public class ListMultipartUploadsResponse extends
		Ks3WebServiceXmlResponse<ListMultipartUploadsResult> {

	private MultiPartUploadInfo upload = null;
	private Owner owner = null;

	public int[] expectedStatus() {
		return new int[] { 200 };
	}

	@Override
	public void preHandle() {

	}

	@Override
	public void startDocument() throws SAXException {
		result = new ListMultipartUploadsResult();
		result.setCommonPrefixes(new ArrayList<String>());
		result.setUploads(new ArrayList<MultiPartUploadInfo>());
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("Upload".equals(getTag())) {
			this.upload = new MultiPartUploadInfo();
		} else if ("Initiator".equals(getTag())) {
			this.owner = new Owner();
		} else if ("Owner".equals(getTag())) {
			this.owner = new Owner();
		}
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		if ("Upload".equals(getTag())) {
			this.result.getUploads().add(upload);
		} else if ("Initiator".equals(getTag())) {
			this.upload.setInitiator(owner);
		} else if ("Owner".equals(getTag())) {
			this.upload.setOwner(owner);
		}
	}

	@Override
	public void string(String s) {
		if ("ListMultipartUploadsResult".equals(getTag(1))) {
			if ("Bucket".equals(getTag())) {
				this.result.setBucket(s);
			} else if ("KeyMarker".equals(getTag())) {
				this.result.setKeyMarker(s);
			} else if ("UploadIdMarker".equals(getTag())) {
				this.result.setUploadIdMarker(s);
			} else if ("NextKeyMarker".equals(getTag())) {
				this.result.setNextKeyMarker(s);
			} else if ("NextUploadIdMarker".equals(getTag())) {
				this.result.setNextUploadIdMarker(s);
			} else if ("Encoding-Type".equals(getTag())) {
				this.result.setEncodingType(s);
			} else if ("MaxUploads".equals(getTag())) {
				this.result.setMaxUploads(Integer.valueOf(s));
			} else if ("IsTruncated".equals(getTag())) {
				this.result.setTruncated("true".equals(s));
			} else if ("Delimiter".equals(getTag())) {
				this.result.setDelimiter(s);
			} else if("Prefix".equals(getTag())){
				this.result.setPrefix(s);
			}
		}else if("Upload".equals(getTag(1))){
			if("Key".equals(getTag())){
				this.upload.setKey(s);
			}else if("UploadId".equals(getTag())){
				this.upload.setUploadId(s);
			}else if("StorageClass".equals(getTag())){
				this.upload.setStorageClass(s);
			}else if("Initiated".equals(getTag())){
				this.upload.setInitiated(DateUtils.convertStr2Date(s));
			}
		}else if("Owner".equals(getTag(1))||"Initiator".equals(getTag(1))){
			if("ID".equals(getTag())){
				this.owner.setId(s);
			}else if("DisplayName".equals(getTag())){
				this.owner.setDisplayName(s);
			}
		}else if("CommonPrefixes".equals(getTag(1))){
			this.result.getCommonPrefixes().add(s);
		}
	}

}
