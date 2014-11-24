package com.ksyun.ks3.demo;

import com.ksyun.ks3.config.ClientConfig;
import com.ksyun.ks3.config.ConfigLoader;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月24日 上午10:51:09
 * 
 * @description 
 **/
public class DemoConfigLoader implements ConfigLoader{

	public ClientConfig load(ClientConfig config) {
		//设置http最大重试次数
		config.set(ClientConfig.MAX_RETRY,"4");
		return config;
	}

}
