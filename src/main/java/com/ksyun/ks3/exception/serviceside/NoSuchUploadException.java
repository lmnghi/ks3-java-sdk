package com.ksyun.ks3.exception.serviceside;
import com.ksyun.ks3.exception.Ks3ServiceException;
/**
 * @author lijunwei[lijunwei@kingsoft.com] 
 * @date 2014年11月7日 上午10:39:47
 * @description The specified multipart upload does not exist. The upload ID might be invalid, or the multipart upload might have been aborted or completed.
 **/
public class NoSuchUploadException extends Ks3ServiceException{
private static final long serialVersionUID = 2177914202944479049L;
}