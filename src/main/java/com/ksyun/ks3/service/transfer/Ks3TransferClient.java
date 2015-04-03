package com.ksyun.ks3.service.transfer;

import java.io.File;

import com.ksyun.ks3.service.Ks3Client;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月3日 下午1:28:32
 * 
 * @description 对{@link Ks3Client}的封装
 **/
public class Ks3TransferClient {
	Ks3Client client;
	public Ks3TransferClient(Ks3Client client){
		this.client = client;
	}
	/**
	 * 根据文件大小自动选择普通上传或分块上传
	 * @param bucket
	 * @param key
	 * @param file
	 */
	public void uploadFile(String bucket,String key,File file){
		
	}
	/**
	 * 
	 */
	public void uploadDir(){
		
	}
}
