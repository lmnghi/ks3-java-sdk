package com.ksyun.ks3.encryption;

import java.security.KeyPair;

import javax.crypto.SecretKey;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年3月30日 下午2:28:47
 * 
 * @description 用来加密的秘钥
 **/
public class EncryptionMaterials {
	//非对称秘钥
    private final KeyPair keyPair;
    //对称秘钥
    private final SecretKey symmetricKey;
    //使用非对称秘钥
    public EncryptionMaterials(KeyPair keyPair){
    	this.keyPair = keyPair;
    	symmetricKey = null;
    }
    //使用对称秘钥
    public EncryptionMaterials(SecretKey symmetricKey){
    	this.keyPair = null;
    	this.symmetricKey = symmetricKey;
    }
	public KeyPair getKeyPair() {
		return keyPair;
	}
	public SecretKey getSymmetricKey() {
		return symmetricKey;
	}
}
