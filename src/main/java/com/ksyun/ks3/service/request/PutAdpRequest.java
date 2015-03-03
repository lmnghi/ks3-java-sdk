package com.ksyun.ks3.service.request;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ksyun.ks3.dto.Adp;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.utils.Base64;
import com.ksyun.ks3.utils.HttpUtils;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月2日 下午4:30:13
 * 
 * @description 添加数据处理任务
 **/
public class PutAdpRequest extends Ks3WebServiceRequest{

	/**
	 * 要进行的处理任务
	 */
	private List<Adp> adps = new ArrayList<Adp>();
	/**
	 * 数据处理任务完成后通知的url
	 */
	private String notifyURL;
	/**
	 * 
	 * @param bucketName 要处理的数据所在bucket
	 * @param key 要处理的数据的key
	 */
	public PutAdpRequest(String bucketName,String key){
		super.setBucketname(bucketName);
		super.setObjectkey(key);
	}
	/**
	 * 
	 * @param bucketName 要处理的数据所在bucket
	 * @param key 要处理的数据的key
	 * @param fops 数据处理指令
	 */
	public PutAdpRequest(String bucketName,String key,List<Adp> fops){
		super.setBucketname(bucketName);
		super.setObjectkey(key);
		this.setAdps(fops);
	}
	@Override
	protected void configHttpRequest() {
		this.addParams("adp", "");
		this.addHeader(HttpHeaders.AsynchronousProcessingList, URLEncoder.encode(HttpUtils.convertAdps2String(adps)));
		if(!StringUtils.isBlank(notifyURL))
			this.addHeader(HttpHeaders.NotifyURL, notifyURL);
		this.setHttpMethod(HttpMethod.PUT);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if(StringUtils.isBlank(this.getBucketname()))
			throw notNull("bucketname");
		if(StringUtils.isBlank(this.getObjectkey()))
			throw notNull("objectkey");
		if(adps==null){
			throw notNull("adps");
		}else{
			for(Adp adp : adps){
				if(StringUtils.isBlank(adp.getCommand())){
					throw notNull("adps.command");
				}
			}
		}
		if(StringUtils.isBlank(notifyURL))
			throw notNull("notifyURL");
	}

	public List<Adp> getAdps() {
		return adps;
	}

	public void setAdps(List<Adp> adps) {
		this.adps = adps;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

}
