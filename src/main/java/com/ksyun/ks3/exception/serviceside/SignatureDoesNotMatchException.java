package com.ksyun.ks3.exception.serviceside;
import com.ksyun.ks3.exception.Ks3ServiceException;
/**
 * @author lijunwei[lijunwei@kingsoft.com] 
 * @date 2014年11月7日 上午10:39:47
 * @description The request signature we calculated does not match the signature you provided. Check your KS3 secret access key and signing method. For more information, see REST Authentication and SOAP Authentication for details.
 **/
public class SignatureDoesNotMatchException extends Ks3ServiceException{
private static final long serialVersionUID = 2177914202944479049L;
}