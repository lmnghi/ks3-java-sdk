package com.ksyun.ks3.dto;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午6:09:15
 * 
 * @description 
 **/
public class GetObjectResult {
	private Ks3Object object = new Ks3Object();
	/**
	 * false 
	 * object为null
	 */
	private boolean ifModified = true;
	/**
	 * false
	 * object为null
	 */
	private boolean ifPreconditionSuccess = true;
	public String toString()
	{
		return "GetObjectResult[ifModified="+this.isIfModified()+",ifPreconditionSuccess="+this.isIfPreconditionSuccess()+",Object="+this.object+"]";
	}
	public Ks3Object getObject() {
		return object;
	}
	public void setObject(Ks3Object object) {
		this.object = object;
	}
	public boolean isIfModified() {
		return ifModified;
	}
	public void setIfModified(boolean ifModified) {
		this.ifModified = ifModified;
	}
	public boolean isIfPreconditionSuccess() {
		return ifPreconditionSuccess;
	}
	public void setIfPreconditionSuccess(boolean ifPreconditionSuccess) {
		this.ifPreconditionSuccess = ifPreconditionSuccess;
	}
	
}
