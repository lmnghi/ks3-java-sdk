package com.ksyun.ks3.service.response;

import com.ksyun.ks3.dto.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨春建 on 2014/10/20.
 */
public class GetBucketACLResponse extends Ks3WebServiceXmlResponse<AccessControlPolicy> {
    private boolean isAdd = false;
    private Grantee grantee=null;
    private Grant grant = null;
    private AccessControlList accessControlList = null;
    private Owner owner;
    private Permission permission = null;

    @Override
    public void preHandle() {

    }

    @Override
    public void startDocument() throws SAXException {
        result = new AccessControlPolicy();
    }

    @Override
    public void startEle(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("Owner".equalsIgnoreCase(getTag())){
            owner = new Owner();
        }
        if("Grant".equalsIgnoreCase(getTag())){
            grant = new Grant();
        }
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
        if("AccessControlList".equalsIgnoreCase(getTag())){
            accessControlList = new AccessControlList();
        }
    }

    @Override
    public void endEle(String uri, String localName, String qName) throws SAXException {

        if("Owner".equalsIgnoreCase(getTag())){
            result.setOwner(owner);
        }
        if("Grantee".equalsIgnoreCase(getTag())){
            grant.setGrantee(grantee);
        }
        if("Permission".equalsIgnoreCase(getTag())){
            grant.setPermission(permission);
        }
        if("Grant".equalsIgnoreCase(getTag())){
            accessControlList.addGrant(grant);
            isAdd = false;
        }
        if("AccessControlList".equalsIgnoreCase(getTag())){
            result.setAccessControlList(accessControlList);
        }
    }

    @Override
    public void string(String s) {
        if("Owner".equalsIgnoreCase(getTag(1))&&"ID".equalsIgnoreCase(getTag()))
        {
            owner.setId(s);
        }
        if("Owner".equalsIgnoreCase(getTag(1))&&"DisplayName".equalsIgnoreCase(getTag()))
        {
            owner.setDisplayName(s);
        }
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
        if("Permission".equalsIgnoreCase(getTag())){
            permission = Permission.load(s);
            grant.setPermission(permission);
        }

    }

    public int[] expectedStatus() {
        return new int[]{200};
    }
}
