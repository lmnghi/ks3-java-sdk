package com.ksyun.ks3.service.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;

import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.dto.GetObjectResult;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.utils.DateUtils;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月20日 下午7:55:52
 * 
 * @description
 **/
public class GetObjectResponse extends
		Ks3WebServiceDefaultResponse<GetObjectResult> {
	private static Log log = LogFactory.getLog(GetObjectResponse.class);

	public int[] expectedStatus() {
		/**
		 * 200 正常 206加rang的时候 304 not modified 412 Precondition Failed
		 */
		return new int[] { 200, 206, 304, 412 };
	}

	@Override
	public void preHandle() {
		ObjectMetadata metaData = new ObjectMetadata();
		result = new GetObjectResult();
		int statusCode = this.getResponse().getStatusLine().getStatusCode();
		if (statusCode == 200 || statusCode == 206) {
			result.getObject().setObjectContent(getContent());
			result.getObject().setRedirectLocation(
					getHeader(HttpHeaders.XKssWebsiteRedirectLocation
							.toString()));
			Header[] headers = this.getResponse().getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				if (headers[i].getName().startsWith(
						Constants.KS3_USER_META_PREFIX)) {
					metaData.setUserMeta(headers[i].getName(),
							headers[i].getValue());
				} else {
					String key = headers[i].getName();
					String value = headers[i].getValue();
					if (Constants.KS3_IGNOREG_HEADERS.contains(key)) {
						// ignore...
					} else if (key.equals(HttpHeaders.LastModified)) {
						try {
							metaData.setLastModified(DateUtils
									.convertStr2Date(key));
						} catch (Exception pe) {
							log.warn("Unable to parse last modified date: "
									+ value, pe);
						}
					} else if (key.equals(HttpHeaders.ContentLength)) {
						try {
							metaData.setHeader(key, Long.parseLong(value));
						} catch (NumberFormatException nfe) {
							log.warn(
									"Unable to parse content length: " + value,
									nfe);
						}
					} else if (key.equals(HttpHeaders.ETag)) {
						metaData.setHeader(key, value.replace("\"", ""));
					} else if (key.equals(HttpHeaders.Expires)) {
						try {
							metaData.setHttpExpiresDate(DateUtils
									.convertStr2Date(value));
						} catch (Exception pe) {
							log.warn("Unable to parse http expiration date: "
									+ value, pe);
						}
					} else {
						metaData.setHeader(key, value);
					}
				}
			}
			result.getObject().setObjectMetadata(metaData);
		}else if(statusCode ==304){
			result.setIfModified(false);
		}else if(statusCode == 412){
			result.setIfPreconditionSuccess(false);
		}
	}

}
