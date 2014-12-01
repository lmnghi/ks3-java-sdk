package com.ksyun.ks3.exception.serviceside;
import com.ksyun.ks3.exception.Ks3ServiceException;
/**
 * @author lijunwei[lijunwei@kingsoft.com] 
 * @date 2014年11月7日 上午10:39:47
 * @description Your previous request to create the named bucket succeeded and you already own it. You get this error in all KS3 regions except US Standard, us-east-1. In us-east-1 region, you will get 200 OK, but it is no-op (if bucket exists it Amazon S3 will not do anything).
 **/
public class BucketAlreadyOwnedByYouException extends Ks3ServiceException{
private static final long serialVersionUID = 2177914202944479049L;
}