package com.ksyun.ks3.service.response;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.BucketLoggingStatus;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.Grantee;
import com.ksyun.ks3.dto.GranteeEmail;
import com.ksyun.ks3.dto.GranteeId;
import com.ksyun.ks3.dto.GranteeUri;
import com.ksyun.ks3.dto.Permission;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年11月16日
 * 
 * @description 
 **/
public class GetBucketLoggingResponse extends Ks3WebServiceXmlResponse<BucketLoggingStatus>{
	private boolean isAdd = false;
	private Grantee grantee = null;
	private Grant grant = null;
	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
	}

	@Override
	public void startDocument() throws SAXException {
		result = new BucketLoggingStatus();
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {	
		if("LoggingEnabled".equals(getTag()))
			result.setEnable(true);
		if("Grant".equals(getTag()))
			this.grant = new Grant();
        if("Grantee".equalsIgnoreCase(getTag(1))){
        	if("EmailAddress".equalsIgnoreCase(getTag())){
                grantee = new GranteeEmail();
        	}else if("URI".equalsIgnoreCase(getTag())){
        		//do nothing
        	}else{
                if(!isAdd) {
                    grantee = new GranteeId();
                    isAdd = true;
                }
        	}
        }
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		if("Grant".equals(getTag()))
			result.getTargetGrants().add(grant);
		else if("Grantee".equals(getTag())){
			grant.setGrantee(grantee);
		}
	}

	@Override
	public void string(String s) {
        if("Grant".equalsIgnoreCase(getTag(2))){
            if("Grantee".equalsIgnoreCase(getTag(1))){
                if("ID".equalsIgnoreCase(getTag())||"EmailAddress".equalsIgnoreCase(getTag())){
                    grantee.setIdentifier(s);
                }
                else if("URI".equalsIgnoreCase(getTag())){
                	grantee = GranteeUri.load(s);
                }
                else if("DisplayName".equalsIgnoreCase(getTag())){
                    ((GranteeId) grantee).setDisplayName(s);
                }
                grant.setGrantee(grantee);
            }
        }
        if("TargetBucket".equals(getTag())){
        	result.setTargetBucket(s);
        }else if("TargetPrefix".equals(getTag())){
        	result.setTargetPrefix(s);
        }else if("Permission".equalsIgnoreCase(getTag())){
            grant.setPermission(Permission.load(s));
        }
	}
}
