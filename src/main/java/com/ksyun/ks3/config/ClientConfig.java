package com.ksyun.ks3.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午5:14:28
 * 
 * @description 
 * <p>客户端的配置类</p>
 * <p>用户在初始化Ks3Client之前需要先进行配置</p>
 * <ul>
 * <li>
 * 可通过
 * {@code 
 * ClientConfig config = ClientConfig.getInstance();
 * config.set(ClientConfig.SOCKET_TIMEOUT,"1000");
 * config.setAccessKeyId(value);
 * config.setAccessKeyIdSecret(value);
 * }
 * 的方式进行配置
 * </li>
 * <li>
 * 可通过
 * {@code
 * 首先新建一个实现{@link ConfigLoader}接口的类,在该类里加载配置（如通过配置文件等）
 * ClientConfig config = ClientConfig.getInstance();
 * ConfigLoader loader = ....
 * config.addConfigLoader(loader);
 * }
 * 的方式配置
 * </li>
 * <li>
 * 也可通过使用该方法初始化 new Ks3Client(String accessKey,String accessKeySecret)
 * </li>
 * </ul>
 **/
public class ClientConfig {
	private static final Log log = LogFactory.getLog(ClientConfig.class); 
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String CONNECTION_TIMEOUT = "httpclient.connectionTimeout";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String SOCKET_TIMEOUT = "httpclient.socketTimeout";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String SOCKET_SEND_BUFFER_SIZE_HINT = "httpclient.socketSendBufferSizeHint";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String SOCKET_RECEIVE_BUFFER_SIZE_HINT = "httpclient.socketReceiveBufferSzieHint";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String CONNECTION_TTL = "httpclient.connnetionTTL";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String MAX_CONNECTIONS = "httpclient.maxConnections";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String PROXY_HOST = "httpclient.proxyHost";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String PROXY_PORT = "httpclient.proxyPort";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String PROXY_USER_NAME = "httpclient.ProxyUserName";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String PROXY_PASSWORD = "httpclient.ProxyPassword";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String PROXY_DAMAIN = "httpclient.ProxyDomain";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String PROXY_WORKSTATION = "httpclient.ProxyWorkStation";
	/**
	 * httpclient配置  值为配置的key
	 */
	public static final String IS_PREEMPTIVE_BASIC_PROXY_AUTH = "httpclient.isPreemptiveBasicProxyAuth";
	
	/**
	 * accessKeyId
	 */
	public static final String ACCESS_KEY_ID = "authorization.accessKeyId";
	/**
	 * accessKeySecret
	 */
	public static final String ACCESS_KEY_SECRET = "authorization.accessKeySecret";
	/**
	 * Ks3服务地址
	 */
	public static final String END_POINT = "ks3client.endpoint";
	/**
	 * ks3 client模式  0：正常模式       1：每执行一次操作都会清空accesskeyid和accesskeysecret信息
	 */
	public static final String CLIENT_MODEL = "ks3client.model";
	/**
	 * 配置加载器列表
	 */
	private static List<ConfigLoader> configLoaders = new ArrayList<ConfigLoader>();
	/**
	 * 下次调用getInstance是否需要调用configLoaders中的ConfigLoader
	 */
	private static boolean reload = true;
	/**
	 * 可以添加自己的ConfigLoader
	 * @param loader {@link ConfigLoader}
	 */
	public static void addConfigLoader(ConfigLoader loader) {
		reload = true;
		configLoaders.add(loader);
	}

	static {
		configLoaders.add(new DefaultConfigLoader());
	}
	/**
	 * 单例的配置
	 */
	private static ClientConfig instance = null;
	/**
	 * 配置内容存储在改map中
	 */
	private Map<String, String> config = new HashMap<String, String>();

	private ClientConfig() {
	}
	/**
	 * 获取配置
	 */
	public static ClientConfig getConfig() {
		synchronized (ClientConfig.class) {
			if (instance == null) {
				instance = new ClientConfig();
			}
			if (reload) {
				for (int i = 0; i < configLoaders.size(); i++) {
					ConfigLoader loader = configLoaders.get(i);
					instance = loader.load(instance);
					log.info("complete load config from "+loader.getClass());
				}
				reload = false;
			}
		}
		return instance;
	}
	/**
	 * 该方法获取到的ClientConfig一定是重新调用configLoaders加载过的
	 */
	public static ClientConfig reloadConfig()
	{
		reload = true;
		return getConfig();
	}
	/**
	 * 以字符串方式返回配置值
	 */
	public String getStr(String key) {
		key = key.toLowerCase();
		if (config.containsKey(key)) {
			return get(key);
		}
		return null;
	}
	/**
	 * 以int方式返回配置值
	 */
	public int getInt(String key) {
		key = key.toLowerCase();
		if (config.containsKey(key)) {
			return Integer.parseInt(get(key));
		}
		return -1;
	}
	/**
	 * 以long方式返回配置值
	 */
	public long getLong(String key) {
		key = key.toLowerCase();
		if (config.containsKey(key)) {
			return Long.parseLong(get(key));
		}
		return -1L;
	}
	/**
	 * 以boolwan方式返回配置值
	 */
	public boolean getBoolean(String key) {
		key = key.toLowerCase();
		return "true".equals(get(key));
	}

	private String get(String key) {
		return config.get(key);
	}

	public void set(String key, String value) {
		if (StringUtils.isBlank(key))
			throw new Ks3ClientException("config key cann't be null");
		if ("null".equals(value))
			value = null;
		config.put(key.toLowerCase(), value);
	}
	/**
	 * 设置AccessKeyId
	 */
	public void setAccessKeyId(String value) {
		set(ClientConfig.ACCESS_KEY_ID, value);
	}
	/**
	 * 设置AccessKeySecret
	 */
	public void setAccessKeySecret(String value) {
		set(ClientConfig.ACCESS_KEY_SECRET, value);
	}
}
