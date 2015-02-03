package com.ksyun.ks3.service.response;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.ksyun.ks3.dto.FopInfo;
import com.ksyun.ks3.dto.FopTask;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午5:28:42
 * 
 * @description 
 **/
public class GetPfopResponse extends Ks3WebServiceXmlResponse<FopTask>{

	private FopInfo fopInfo;
	private List<FopInfo> fopInfos ;
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
		result = new FopTask();
		fopInfos = new ArrayList<FopInfo>();
	}

	@Override
	public void startEle(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if("pfopinfo".equals(getTag())){
			fopInfo = new FopInfo();
		}else if("keys".equals(getTag())){
			keys = new ArrayList<String>();
		}
	}

	@Override
	public void endEle(String uri, String localName, String qName)
			throws SAXException {
		if("Task".equals(getTag())){
			result.setFopInfos(fopInfos);
		}
		else if("pfopinfo".equals(getTag())){
			fopInfos.add(fopInfo);
		}else if("keys".equals(getTag())){
			fopInfo.setKeys(keys);
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
			fopInfo.setCommand(s);
		}else if("error".equals(getTag())){
			fopInfo.setDesc(s);
		}else if("desc".equals(getTag())){
			fopInfo.setSuccess("success".equalsIgnoreCase(s));
		}else if("value".equals(getTag())){
			keys.add(s);
		}
	}

}
