package com.ksyun.ks3.signer;

import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;
import com.ksyun.ks3.utils.AuthUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月6日 上午10:45:56
 * 
 * @description 默认的签名计算器
 **/
public class DefaultSigner implements Signer {

	public String calculate(Authorization auth, Ks3WebServiceRequest request) {
		try {
			return AuthUtils.calcAuthorization(auth, request);
		} catch (Exception e) {
			throw new Ks3ClientException(
					"calculate user authorization has occured an exception ("
							+ e + ")", e);
		}
	}

}
