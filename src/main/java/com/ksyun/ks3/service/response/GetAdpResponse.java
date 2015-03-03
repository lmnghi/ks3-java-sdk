package com.ksyun.ks3.service.response;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.AdpInfo;
import com.ksyun.ks3.dto.AdpTask;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午5:28:42
 * 
 * @description 
 **/
public class GetAdpResponse extends Ks3WebServiceXmlResponse<AdpTask>{

	private AdpInfo adpInfo;
	private List<AdpInfo> adpInfos ;
	private List<String> keys;
	public int[] expectedStatus() {
		return new int[]{200};
	}

	@Override
	public void preHandle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		result = new AdpTask();
		adpInfos = new ArrayList<AdpInfo>();
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("pfopinfo".equals(getTag())){
			adpInfo = new AdpInfo();
		}else if("keys".equals(getTag())){
			keys = new ArrayList<String>();
		}
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		if("Task".equals(getTag())){
			result.setAdpInfos(adpInfos);
		}
		else if("pfopinfo".equals(getTag())){
			adpInfos.add(adpInfo);
		}else if("keys".equals(getTag())){
			adpInfo.setKeys(keys);
		}
	}

	@Override
	public void string(String s) {
		if("taskid".equals(getTag())){
			result.setTaskId(s);
		}else if("processstatus".equals(getTag())){
			result.setProcessstatus(s);
		}else if("processdesc".equals(getTag())){
			result.setProcessdesc(s);
		}else if("notifystatus".equals(getTag())){
			result.setNotifystatus(s);
		}else if("notifydesc".equals(getTag())){
			result.setNotifydesc(s);
		}else if("cmd".equals(getTag())){
			adpInfo.setCommand(s);
		}else if("error".equals(getTag())){
			adpInfo.setDesc(s);
		}else if("desc".equals(getTag())){
			adpInfo.setSuccess("success".equalsIgnoreCase(s));
		}else if("value".equals(getTag())){
			keys.add(s);
		}
	}

}
