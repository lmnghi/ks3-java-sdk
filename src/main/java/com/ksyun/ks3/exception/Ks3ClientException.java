package com.ksyun.ks3.exception;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月14日 下午5:52:58
 * 
 * @description 当客户端抛出该异常时表示Ks3客户端发生了异常
 **/
public class Ks3ClientException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2503345001841814995L;

	
	public Ks3ClientException(String message, Throwable t) {
        super(message, t);
    }
    public Ks3ClientException(String message) {
        super(message);
    }

    public Ks3ClientException(Throwable t) {
        super(t);
    }
}
