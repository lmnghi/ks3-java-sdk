package com.ksyun.ks3.utils;

import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.config.Constants;


/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 *
 * @date 2014年10月15日 下午4:53:56
 *
 * @description
 **/
public class XmlWriter {
    private List<String> tag = new ArrayList<String>();
    private StringBuffer buffer = new StringBuffer();

    public XmlWriter start(String nodeName) {
        buffer.append("<" + nodeName + ">");
        this.tag.add(nodeName);
        return this;
    }

    public XmlWriter start(String nodeName, String param, String value) {
        buffer.append("<" + nodeName + " " + param + "=\"" + value + "\">");
        this.tag.add(nodeName);
        return this;
    }
    public XmlWriter start(String nodeName, String[] params, String[] values) {
    	if(params.length!=values.length)
    		throw new IllegalArgumentException("params.length should be equals with values.length");
    	
        buffer.append("<" + nodeName+" ");
        for(int i =0;i<params.length;i++){
        	buffer.append(params[i]+"=\""+values[i]+"\" ");
        }
        buffer.append(">");
        this.tag.add(nodeName);
        return this;
    }

    public XmlWriter startWithNs(String nodeName) {
        return start(nodeName, "xmlns", Constants.KS3_XML_NAMESPACE);
    }
    public XmlWriter end() {
        buffer.append("</" + tag.get(tag.size() - 1) + ">");
        tag.remove(tag.size() - 1);
        return this;
    }

    public XmlWriter value(String value) {
        buffer.append(value);
        return this;
    }
    public XmlWriter value(int value) {
        buffer.append(value);
        return this;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}