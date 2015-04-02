package com.ksyun.ks3.encryption;

import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.dto.Ks3Object;
import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.encryption.config.CryptoConfiguration;
import com.ksyun.ks3.encryption.internal.CryptoModuleDispatcher;
import com.ksyun.ks3.encryption.internal.S3CryptoModule;
import com.ksyun.ks3.encryption.materials.EncryptionMaterials;
import com.ksyun.ks3.encryption.materials.EncryptionMaterialsProvider;
import com.ksyun.ks3.encryption.materials.StaticEncryptionMaterialsProvider;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.request.GetObjectRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.*;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年3月30日 下午1:54:28
 * 
 * @description 通过客户端加密，解密的方法上传下载文件
 **/
public class Ks3EncryptionClient extends Ks3Client implements Ks3{
	private S3CryptoModule crypto = null;
	
	
	public Ks3EncryptionClient(Authorization auth,EncryptionMaterials encryptionMaterials) {
		this(auth,new StaticEncryptionMaterialsProvider(encryptionMaterials));
	}
	public Ks3EncryptionClient(Authorization auth,EncryptionMaterialsProvider encryptionMaterialsProvider) {
		this(auth,encryptionMaterialsProvider,new CryptoConfiguration());
	}
	public Ks3EncryptionClient(Authorization auth,EncryptionMaterials encryptionMaterials,CryptoConfiguration cryptoConfig){
		this(auth,new StaticEncryptionMaterialsProvider(encryptionMaterials),cryptoConfig);
	}
	public Ks3EncryptionClient(Authorization auth,EncryptionMaterialsProvider encryptionMaterialsProvider,CryptoConfiguration cryptoConfig){
		super(auth);
		if(encryptionMaterialsProvider==null)
			throw notNull("encryptionMaterials");
		if(cryptoConfig == null)
			throw notNull("cryptoConfig");
		this.crypto = new CryptoModuleDispatcher(new Ks3Client(auth), encryptionMaterialsProvider, cryptoConfig);
    }
	/**
	 * 使用客户端加密的方式上传文件
	 * @param req
	 * @return
	 */
    public PutObjectResult putObjectSecurely(PutObjectRequest req) {
        return crypto.putObjectSecurely(req);
    }
    public Ks3Object getObjectSecurely(GetObjectRequest req) {
        return crypto.getObjectSecurely(req);
    }
}
