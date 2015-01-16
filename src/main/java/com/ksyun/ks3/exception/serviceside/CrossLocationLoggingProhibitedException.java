package com.ksyun.ks3.exception.serviceside;
import com.ksyun.ks3.exception.Ks3ServiceException;
/**
 * @author lijunwei[lijunwei@kingsoft.com] 
 * @date 2014年11月7日 上午10:39:47
 * @description Cross-location logging not allowed. Buckets in one geographic location cannot log information to a bucket in another location.
 **/
public class CrossLocationLoggingProhibitedException extends Ks3ServiceException{
private static final long serialVersionUID = 2177914202944479049L;
}