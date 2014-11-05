package com.ksyun.ks3.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;

import com.ksyun.ks3.config.ClientConfig;


/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午8:35:12
 * 
 * @description 
 **/
public class ConnectionManagerFactory {
    public static PoolingClientConnectionManager createPoolingClientConnManager( HttpParams httpClientParams ) {
    	ClientConfig config = ClientConfig.getConfig();
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(
                SchemeRegistryFactory.createDefault(),
                config.getLong(ClientConfig.CONNECTION_TTL), TimeUnit.MILLISECONDS);
        connectionManager.setDefaultMaxPerRoute(config.getInt(ClientConfig.MAX_CONNECTIONS));
        connectionManager.setMaxTotal(config.getInt(ClientConfig.MAX_CONNECTIONS));
        return connectionManager;
    }
}
