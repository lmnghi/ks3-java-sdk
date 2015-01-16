package com.ksyun.ks3.exception.serviceside;
import com.ksyun.ks3.exception.Ks3ServiceException;
/**
 * @author lijunwei[lijunwei@kingsoft.com] 
 * @date 2014年11月7日 上午10:39:47
 * @description This happens when the user sends an empty xml document as a request. The error message is, \
 **/
public class MissingRequestBodyErrorException extends Ks3ServiceException{
private static final long serialVersionUID = 2177914202944479049L;
}