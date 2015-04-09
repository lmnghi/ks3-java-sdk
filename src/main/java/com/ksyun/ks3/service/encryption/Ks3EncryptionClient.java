package com.ksyun.ks3.service.encryption;

import java.io.File;

import com.ksyun.ks3.dto.PutObjectResult;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.encryption.internal.CryptoModuleDispatcher;
import com.ksyun.ks3.service.encryption.internal.S3CryptoModule;
import com.ksyun.ks3.service.encryption.model.CryptoConfiguration;
import com.ksyun.ks3.service.encryption.model.EncryptionMaterials;
import com.ksyun.ks3.service.encryption.model.EncryptionMaterialsProvider;
import com.ksyun.ks3.service.encryption.model.StaticEncryptionMaterialsProvider;
import com.ksyun.ks3.service.request.PutObjectRequest;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年4月8日 上午10:30:50
 * 
 * @description 
 **/
public class Ks3EncryptionClient extends Ks3Client{
    private final S3CryptoModule<?> crypto;

    // ///////////////////// Constructors ////////////////
    public Ks3EncryptionClient(
    		String accesskeyid,
            String accesskeysecret,
            EncryptionMaterials encryptionMaterials) {
        this(accesskeyid,accesskeysecret, new StaticEncryptionMaterialsProvider(
                encryptionMaterials));
    }
    public Ks3EncryptionClient(
    		String accesskeyid,
            String accesskeysecret,
            EncryptionMaterialsProvider encryptionMaterialsProvider) {
        this(accesskeyid,accesskeysecret, encryptionMaterialsProvider,
                 new CryptoConfiguration());
    }


    public Ks3EncryptionClient(
    		String accesskeyid,
            String accesskeysecret,
            EncryptionMaterials encryptionMaterials,
            CryptoConfiguration cryptoConfig) {
        this(accesskeyid,accesskeysecret, new StaticEncryptionMaterialsProvider(
                encryptionMaterials), cryptoConfig);
    }


    public Ks3EncryptionClient(
            String accesskeyid,
            String accesskeysecret,
            EncryptionMaterialsProvider kekMaterialsProvider,
            CryptoConfiguration cryptoConfig) {
        super(accesskeyid, accesskeysecret);
        assertParameterNotNull(kekMaterialsProvider,
                "EncryptionMaterialsProvider parameter must not be null.");
        assertParameterNotNull(cryptoConfig,
                "CryptoConfiguration parameter must not be null.");
        this.crypto = new CryptoModuleDispatcher(new S3DirectImpl(),
                 kekMaterialsProvider
                 ,cryptoConfig);
    }

    private void assertParameterNotNull(Object parameterValue,
            String errorMessage) {
        if (parameterValue == null)
            throw new IllegalArgumentException(errorMessage);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Use {@link EncryptedPutObjectRequest} to specify materialsDescription for the EncryptionMaterials to be used for 
     * this request.Ks3EncryptionClient would use {@link EncryptionMaterialsProvider#getEncryptionMaterials(java.util.Map)} to 
     * retrieve encryption materials corresponding to the materialsDescription specified in the current request.
     * </p>
     * 
     */
    @Override
    public PutObjectResult putObject(PutObjectRequest req) {
        return crypto.putObjectSecurely(req);
    }

    @Override
    public S3Object getObject(GetObjectRequest req) {
        return crypto.getObjectSecurely(req);
    }

    @Override
    public ObjectMetadata getObject(GetObjectRequest req, File dest) {
        return crypto.getObjectSecurely(req, dest);
    }

    @Override
    public void deleteObject(DeleteObjectRequest req) {
        req.getRequestClientOptions().appendUserAgent(USER_AGENT);
        // Delete the object
        super.deleteObject(req);
        // If it exists, delete the instruction file.
        DeleteObjectRequest instructionDeleteRequest = EncryptionUtils
                .createInstructionDeleteObjectRequest(req);
        super.deleteObject(instructionDeleteRequest);
    }

    @Override
    public CompleteMultipartUploadResult completeMultipartUpload(
            CompleteMultipartUploadRequest req) {
        return crypto.completeMultipartUploadSecurely(req);
    }

    /** 
     * {@inheritDoc}
     * <p>
     * Use {@link EncryptedInitiateMultipartUploadRequest} to specify materialsDescription for the EncryptionMaterials to be used for this request.
     * Ks3EncryptionClient would use {@link EncryptionMaterialsProvider#getEncryptionMaterials(java.util.Map)} to retrieve encryption materials
     * corresponding to the materialsDescription specified in the current request.
     * </p>
     */
    @Override
    public InitiateMultipartUploadResult initiateMultipartUpload(
            InitiateMultipartUploadRequest req) {
        return crypto.initiateMultipartUploadSecurely(req);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <b>NOTE:</b> Because the encryption process requires context from block
     * N-1 in order to encrypt block N, parts uploaded with the
     * Ks3EncryptionClient (as opposed to the normal AmazonS3Client) must
     * be uploaded serially, and in order. Otherwise, the previous encryption
     * context isn't available to use when encrypting the current part.
     */
    @Override
    public UploadPartResult uploadPart(UploadPartRequest uploadPartRequest)
            throws AmazonClientException, AmazonServiceException {
        return crypto.uploadPartSecurely(uploadPartRequest);
    }

    @Override
    public CopyPartResult copyPart(CopyPartRequest copyPartRequest) {
        return crypto.copyPartSecurely(copyPartRequest);
    }

    @Override
    public void abortMultipartUpload(AbortMultipartUploadRequest req) {
        crypto.abortMultipartUploadSecurely(req);
    }

    // /////////////////// Access to the methods in the super class //////////
    /**
     * An internal implementation used to provide limited but direct access to
     * the underlying methods of AmazonS3Client without any encryption or
     * decryption operations.
     */
    private final class S3DirectImpl extends S3Direct {
        @Override
        public PutObjectResult putObject(PutObjectRequest req) {
            return Ks3EncryptionClient.super.putObject(req);
        }

        @Override
        public S3Object getObject(GetObjectRequest req) {
            return Ks3EncryptionClient.super.getObject(req);
        }

        @Override
        public ObjectMetadata getObject(GetObjectRequest req, File dest) {
            return Ks3EncryptionClient.super.getObject(req, dest);
        }

        @Override
        public CompleteMultipartUploadResult completeMultipartUpload(
                CompleteMultipartUploadRequest req) {
            return Ks3EncryptionClient.super.completeMultipartUpload(req);
        }

        @Override
        public InitiateMultipartUploadResult initiateMultipartUpload(
                InitiateMultipartUploadRequest req) {
            return Ks3EncryptionClient.super.initiateMultipartUpload(req);
        }

        @Override
        public UploadPartResult uploadPart(UploadPartRequest req)
                throws AmazonClientException, AmazonServiceException {
            return Ks3EncryptionClient.super.uploadPart(req);
        }

        @Override
        public CopyPartResult copyPart(CopyPartRequest req) {
            return Ks3EncryptionClient.super.copyPart(req);
        }

        @Override
        public void abortMultipartUpload(AbortMultipartUploadRequest req) {
            Ks3EncryptionClient.super.abortMultipartUpload(req);
        }
    }
}
