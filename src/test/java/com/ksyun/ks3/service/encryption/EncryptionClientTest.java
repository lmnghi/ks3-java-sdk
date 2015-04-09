package com.ksyun.ks3.service.encryption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.junit.Before;

import com.ksyun.ks3.service.Ks3ClientTest;
import com.ksyun.ks3.service.encryption.model.CryptoConfiguration;
import com.ksyun.ks3.service.encryption.model.CryptoMode;
import com.ksyun.ks3.service.encryption.model.CryptoStorageMode;
import com.ksyun.ks3.service.encryption.model.EncryptionMaterials;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月9日 下午2:20:50
 * 
 * @description 
 **/
public class EncryptionClientTest extends Ks3ClientTest{
	protected Ks3EncryptionClient eo_file;
	protected Ks3EncryptionClient eo_meta;
	protected Ks3EncryptionClient ae_file;
	protected Ks3EncryptionClient ae_meta;
	protected Ks3EncryptionClient sae_file;
	protected Ks3EncryptionClient sae_meta;
	protected String bucket = "test-encryption";
	@Before
	public void initEncryption() throws NoSuchAlgorithmException{
		client1.clearBucket(bucket);
		KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
	    symKeyGenerator.init(256); 
	    SecretKey symKey = symKeyGenerator.generateKey();
	    EncryptionMaterials keyMaterials = new EncryptionMaterials(symKey);
		/*SecureRandom srand = new SecureRandom();
		KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024, srand);
        KeyPair pair = keyGenerator.generateKeyPair();
        EncryptionMaterials keyMaterials = new EncryptionMaterials(pair);*/
	    
		CryptoConfiguration eo_file_config = new CryptoConfiguration();
		eo_file_config.setCryptoMode(CryptoMode.EncryptionOnly);
		eo_file_config.setStorageMode(CryptoStorageMode.InstructionFile);
		this.eo_file = new Ks3EncryptionClient(super.auth1.getAccessKeyId(),super.auth1.getAccessKeySecret(),keyMaterials,eo_file_config);
		
		CryptoConfiguration eo_meta_config = new CryptoConfiguration();
		eo_meta_config.setCryptoMode(CryptoMode.EncryptionOnly);
		eo_meta_config.setStorageMode(CryptoStorageMode.ObjectMetadata);
		this.eo_meta = new Ks3EncryptionClient(super.auth1.getAccessKeyId(),super.auth1.getAccessKeySecret(),keyMaterials,eo_meta_config);
		
		CryptoConfiguration ae_file_config = new CryptoConfiguration();
		ae_file_config.setCryptoMode(CryptoMode.AuthenticatedEncryption);
		ae_file_config.setStorageMode(CryptoStorageMode.InstructionFile);
		this.ae_file = new Ks3EncryptionClient(super.auth1.getAccessKeyId(),super.auth1.getAccessKeySecret(),keyMaterials,ae_file_config);
		
		CryptoConfiguration ae_meta_config = new CryptoConfiguration();
		ae_meta_config.setCryptoMode(CryptoMode.AuthenticatedEncryption);
		ae_meta_config.setStorageMode(CryptoStorageMode.ObjectMetadata);
		this.ae_meta = new Ks3EncryptionClient(super.auth1.getAccessKeyId(),super.auth1.getAccessKeySecret(),keyMaterials,ae_meta_config);
		
		CryptoConfiguration sae_file_config = new CryptoConfiguration();
		sae_file_config.setCryptoMode(CryptoMode.StrictAuthenticatedEncryption);
		sae_file_config.setStorageMode(CryptoStorageMode.InstructionFile);
		this.sae_file = new Ks3EncryptionClient(super.auth1.getAccessKeyId(),super.auth1.getAccessKeySecret(),keyMaterials,sae_file_config);
		
		CryptoConfiguration sae_meta_config = new CryptoConfiguration();
		sae_meta_config.setCryptoMode(CryptoMode.StrictAuthenticatedEncryption);
		sae_meta_config.setStorageMode(CryptoStorageMode.ObjectMetadata);
		this.sae_meta = new Ks3EncryptionClient(super.auth1.getAccessKeyId(),super.auth1.getAccessKeySecret(),keyMaterials,sae_meta_config);
	}
	protected void writeToFile(InputStream content,File file) throws IOException{
		OutputStream os = new FileOutputStream(file);

		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		try {
			while ((bytesRead = content.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
