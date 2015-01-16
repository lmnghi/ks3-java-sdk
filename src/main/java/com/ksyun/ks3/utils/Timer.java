package com.ksyun.ks3.utils;

import java.util.Date;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月20日 下午6:24:02
 * 
 * @description 
 **/
public class Timer {
	private static long t;
	public static Long start()
	{
		t = new Date().getTime();
		return t;
	}
	public static Long end()
	{
		return new Date().getTime() - t;
	}
}
