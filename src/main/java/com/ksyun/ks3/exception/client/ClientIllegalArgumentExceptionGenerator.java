package com.ksyun.ks3.exception.client;

import com.ksyun.ks3.exception.client.ClientIllegalArgumentException.Reason;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年12月3日 下午12:59:26
 * 
 * @description 
 **/
public class ClientIllegalArgumentExceptionGenerator {
	private static String notNull = "参数 %s 不能为空";
	private static String notBothNull = "参数 %s和%s 不能全为空";
	private static String notNullInCondition = "参数 %s在%s 情况下不能为空";
	private static String notCorrect = "参数 %s(%s) 格式错误,正确格式为:%s";
	private static String between = "参数 %s(%s) 应该在%s-%s这个范围内";
	public static ClientIllegalArgumentException notNull(String paramName){
		ClientIllegalArgumentException e = new ClientIllegalArgumentException(String.format(notNull, paramName));
		e.setReason(Reason.notNull);
		e.setParamName(paramName);
		return e;
	}
	public static ClientIllegalArgumentException notNull(String paramName,String paramName1){
		ClientIllegalArgumentException e = new ClientIllegalArgumentException(String.format(notBothNull, paramName,paramName1));
		e.setReason(Reason.notNull);
		e.setParamName(paramName);
		return e;
	}
	public static ClientIllegalArgumentException notNullInCondition(String paramName,String condition){
		ClientIllegalArgumentException e = new ClientIllegalArgumentException(String.format(notNullInCondition, paramName,condition));
		e.setReason(Reason.notNull);
		e.setParamName(paramName);
		return e;
	}
	public static ClientIllegalArgumentException notCorrect(String paramName,String paramValue,String format){
		ClientIllegalArgumentException e = new ClientIllegalArgumentException(String.format(notCorrect, paramName,paramValue,format));
		e.setReason(Reason.notCorrect);
		e.setParamName(paramName);
		return e;
	}
	public static ClientIllegalArgumentException between(String paramName,String paramValue,String min,String max){
		ClientIllegalArgumentException e = new ClientIllegalArgumentException(String.format(between, paramName,paramValue,min,max));
		e.setReason(Reason.notBetween);
		e.setParamName(paramName);
		return e;
	}
}
