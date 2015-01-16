package com.ksyun.ks3.exception.serviceside;
import com.ksyun.ks3.exception.Ks3ServiceException;
/**
 * @author lijunwei[lijunwei@kingsoft.com] 
 * @date 2014年11月7日 上午10:39:47
 * @description The bucket you are attempting to access must be addressed using the specified endpoint. Send all future requests to this endpoint.
 **/
public class PermanentRedirectException extends Ks3ServiceException{
private static final long serialVersionUID = 2177914202944479049L;
}