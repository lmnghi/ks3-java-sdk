package com.ksyun.ks3.config;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月14日 下午5:13:46
 * 
 * @description <p>配置加载器，可以自定义配置加载器，然后通过调用ClientConfig.addConfigLoader(...)。之后便会根据用户在自定义的配置加载器中定义的方法加载配置</p>
 **/
public interface ConfigLoader {
	/**
	 * 在此方法中应该调用config.set(key,value);去设置配置值
	 */
	public ClientConfig load(ClientConfig config);
}
