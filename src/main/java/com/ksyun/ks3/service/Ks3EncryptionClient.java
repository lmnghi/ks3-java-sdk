package com.ksyun.ks3.service;

import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.encryption.EncryptionMaterials;
import com.ksyun.ks3.service.request.PutObjectRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年3月30日 下午1:54:28
 * 
 * @description 通过客户端加密，解密的方法上传下载文件
 **/
public class Ks3EncryptionClient extends Ks3Client implements Ks3{
	private EncryptionMaterials encryptionMaterials = null;
	public Ks3EncryptionClient(Authorization auth,EncryptionMaterials encryptionMaterials) {
		this(auth.getAccessKeyId(),auth.getAccessKeySecret(),encryptionMaterials);
	}
	public Ks3EncryptionClient(String accesskeyid, String accesskeysecret,EncryptionMaterials encryptionMaterials) {
		super(accesskeyid, accesskeysecret);
		this.encryptionMaterials = encryptionMaterials;
	}

	public PutObjectResult putObject(PutObjectRequest request){
		return super.putObject(request);
	}
}
