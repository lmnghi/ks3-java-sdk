package com.ksyun.ks3;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月13日 下午8:55:51
 * 
 * @description 
 **/
public class AutoAbortInputStream extends FilterInputStream{
	private HttpRequest request;
	public AutoAbortInputStream(InputStream in,HttpRequest request) {
		super(in);
		this.request = request;
	}
	@Override
	public int read() throws IOException {
		int ch = super.read();
		if(ch==-1){
			this.abort();
		}
		return ch;
	}
	public void abort(){
		if(this.request instanceof HttpRequestBase){
			((HttpRequestBase) request).abort();
		}
	}
}
