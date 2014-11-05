package com.ksyun.ks3.utils;

import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.config.Constants;


/**
 * @author lijunwei[13810414122@163.com]  
 *
 * @date 2014年10月15日 下午4:53:56
 *
 * @description
 **/
public class XmlWrite {
    private List<String> tag = new ArrayList<String>();
    private StringBuffer buffer = new StringBuffer();

    public XmlWrite start(String nodeName) {
        buffer.append("<" + nodeName + ">");
        this.tag.add(nodeName);
        return this;
    }

    public XmlWrite start(String nodeName, String param, String value) {
        buffer.append("<" + nodeName + " " + param + "=\"" + value + "\">");
        this.tag.add(nodeName);
        return this;
    }

    public XmlWrite startWithNs(String nodeName) {
        return start(nodeName, "xmlns", Constants.KS3_XML_NAMESPACE);
    }

    public XmlWrite end() {
        buffer.append("</" + tag.get(tag.size() - 1) + ">");
        tag.remove(tag.size() - 1);
        return this;
    }

    public XmlWrite value(String value) {
        buffer.append(value);
        return this;
    }
    public XmlWrite value(int value) {
        buffer.append(value);
        return this;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }
}