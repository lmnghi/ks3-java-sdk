package com.ksyun.ks3.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月7日 下午2:02:33
 * 
 * @description 
 **/
public class SdkTester {
	
	private Ks3 client;
	private Ks3WebServiceRequest request;
	private String methodName;
	private Ks3ClientException expectedExceptions;
	private boolean exception = true;
	
	public void Perform() throws Exception
	{
		if(this.expectedExceptions!=null)
			this.exception = false;
		
			Method method = client.getClass().getMethod(methodName, this.request.getClass());
			if(!exception)
			{
				try{
					method.invoke(client, request);
				}catch(Exception e)
				{
					if(e.getCause().getClass().toString().equals(this.expectedExceptions.getClass().toString()))
					{
						exception = true;
					}else{
					    throw e;
					}
				}
			}else{
				method.invoke(client, request);
			}
			if(this.exception==false)
				throw new NotThrowException();
			
		
		
	}
	
	public SdkTester withRequest(Ks3WebServiceRequest request)
	{
		this.request = request;
		return this;
	}
	public SdkTester withMethodName(String method)
	{
		this.methodName = method;
		return this;
	}
	public SdkTester withException(Ks3ClientException exception)
	{
		this.expectedExceptions=exception;
		return this;
	}
	public SdkTester withClient(Ks3 client)
	{
		this.client = client;
		return this;
	}
}
