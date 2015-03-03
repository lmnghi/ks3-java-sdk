package com.ksyun.ks3.request;

import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.between;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notCorrect;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNull;
import static com.ksyun.ks3.exception.client.ClientIllegalArgumentExceptionGenerator.notNullInCondition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ksyun.ks3.MD5DigestCalculatingInputStream;
import com.ksyun.ks3.RepeatableFileInputStream;
import com.ksyun.ks3.RepeatableInputStream;
import com.ksyun.ks3.config.Constants;
import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.CallBackConfiguration;
import com.ksyun.ks3.dto.CannedAccessControlList;
import com.ksyun.ks3.dto.Adp;
import com.ksyun.ks3.dto.ObjectMetadata;
import com.ksyun.ks3.dto.CallBackConfiguration.MagicVariables;
import com.ksyun.ks3.exception.Ks3ClientException;
import com.ksyun.ks3.exception.client.ClientFileNotFoundException;
import com.ksyun.ks3.http.HttpHeaders;
import com.ksyun.ks3.http.HttpMethod;
import com.ksyun.ks3.http.Mimetypes;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;
import com.ksyun.ks3.service.request.PutObjectRequest;
import com.ksyun.ks3.service.request.support.MD5CalculateAble;
import com.ksyun.ks3.utils.HttpUtils;
import com.ksyun.ks3.utils.Md5Utils;
import com.ksyun.ks3.utils.StringUtils;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年2月4日 下午3:11:10
 * 
 * @description
 **/
public class WithOutContentTypePutObjectRequest extends Ks3WebServiceRequest
		implements MD5CalculateAble {
	/**
	 * 要上传的文件
	 */
	private File file;
	/**
	 * 将要上传的object的元数据
	 */
	private ObjectMetadata objectMeta = new ObjectMetadata();
	/**
	 * 设置新的object的acl
	 */
	private CannedAccessControlList cannedAcl;
	/**
	 * 设置新的object的acl
	 */
	private AccessControlList acl = new AccessControlList();
	/**
	 * 设置callback
	 */
	private CallBackConfiguration callBackConfiguration;
	/**
	 * 要进行的数据处理任务
	 */
	private List<Adp> fops = new ArrayList<Adp>();
	/**
	 * 数据处理任务完成后通知的url
	 */
	private String notifyURL;
	private String redirectLocation;

	/**
	 * 
	 * @param bucketname
	 * @param key
	 * @param file
	 *            要上传的文件
	 */
	public WithOutContentTypePutObjectRequest(String bucketname, String key,
			File file) {
		this.setBucketname(bucketname);
		this.setObjectkey(key);
		this.setFile(file);
	}

	/**
	 * 
	 * @param bucketname
	 * @param key
	 * @param inputStream
	 * @param metadata
	 *            请尽量提供content-length,否则可能会导致jvm内存溢出
	 */
	public WithOutContentTypePutObjectRequest(String bucketname, String key,
			InputStream inputStream, ObjectMetadata metadata) {
		this.setBucketname(bucketname);
		this.setObjectkey(key);
		this.setObjectMeta(metadata);
		this.setRequestBody(new RepeatableInputStream(inputStream,
				Constants.DEFAULT_STREAM_BUFFER_SIZE));
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void configHttpRequest() {
		this.setContentType("");
		/**
		 * 设置request body meta
		 */
		if (file != null) {
			try {
				this.setRequestBody(new RepeatableFileInputStream(file));
			} catch (FileNotFoundException e) {
				throw new ClientFileNotFoundException(e);
			}
			if (StringUtils.isBlank(objectMeta.getContentType()))
				objectMeta.setContentType(Mimetypes.getInstance().getMimetype(
						file));
			long length = file.length();
			objectMeta.setContentLength(length);
			try {
				String contentMd5_b64 = Md5Utils.md5AsBase64(file);
				this.objectMeta.setContentMD5(contentMd5_b64);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new ClientFileNotFoundException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new Ks3ClientException("计算文件的MD5值出错 (" + e + ")", e);
			}
		}
		if (this.objectMeta != null) {
			if (!StringUtils.isBlank(this.objectMeta.getContentType()))
				this.addHeader(HttpHeaders.ContentType,
						this.objectMeta.getContentType());
			if (!StringUtils.isBlank(this.objectMeta.getCacheControl()))
				this.addHeader(HttpHeaders.CacheControl,
						this.objectMeta.getCacheControl());
			if (!StringUtils.isBlank(this.objectMeta.getContentDisposition()))
				this.addHeader(HttpHeaders.ContentDisposition,
						this.objectMeta.getContentDisposition());
			if (!StringUtils.isBlank(this.objectMeta.getContentEncoding()))
				this.addHeader(HttpHeaders.ContentEncoding,
						this.objectMeta.getContentEncoding());
			if (this.objectMeta.getContentLength() > 0)
				this.addHeader(HttpHeaders.ContentLength,
						String.valueOf(this.objectMeta.getContentLength()));
			if (this.objectMeta.getHttpExpiresDate() != null)
				this.addHeader(HttpHeaders.Expires, this.objectMeta
						.getHttpExpiresDate().toGMTString());
			if (this.objectMeta.getContentMD5() != null)
				this.addHeader(HttpHeaders.ContentMD5,
						this.objectMeta.getContentMD5());
			// 添加user meta
			for (Entry<String, String> entry : this.objectMeta.getAllUserMeta()
					.entrySet()) {
				if (entry.getKey().startsWith(Constants.KS3_USER_META_PREFIX))
					this.addHeader(entry.getKey(), entry.getValue());
			}
		}
		// acl
		if (this.cannedAcl != null) {
			this.addHeader(HttpHeaders.CannedAcl.toString(),
					cannedAcl.toString());
		}
		if (this.acl != null) {
			this.getHeader().putAll(HttpUtils.convertAcl2Headers(acl));
		}
		if (this.redirectLocation != null) {
			this.addHeader(HttpHeaders.XKssWebsiteRedirectLocation,
					this.redirectLocation);
		}
		if (this.callBackConfiguration != null) {
			this.addHeader(HttpHeaders.XKssCallbackUrl,
					callBackConfiguration.getCallBackUrl());
			StringBuffer body = new StringBuffer();
			if (callBackConfiguration.getBodyMagicVariables() != null) {
				for (Entry<String, MagicVariables> mvs : callBackConfiguration
						.getBodyMagicVariables().entrySet()) {
					body.append(mvs.getKey() + "=${" + mvs.getValue() + "}&");
				}
			}
			if (callBackConfiguration.getBodyKssVariables() != null) {
				for (Entry<String, String> mvs : callBackConfiguration
						.getBodyKssVariables().entrySet()) {
					body.append(mvs.getKey() + "=${kss-" + mvs.getKey() + "}&");
					this.addHeader("kss-" + mvs.getKey(), mvs.getValue());
				}
			}
			String bodyString = body.toString();
			if (bodyString.endsWith("&")) {
				bodyString = bodyString.substring(0, bodyString.length() - 1);
			}
			this.addHeader(HttpHeaders.XKssCallbackBody, bodyString);
		}
		if (this.fops != null && fops.size() > 0) {
			this.addHeader(HttpHeaders.AsynchronousProcessingList,
					URLEncoder.encode(HttpUtils.convertAdps2String(fops)));
			if (!StringUtils.isBlank(notifyURL))
				this.addHeader(HttpHeaders.NotifyURL, notifyURL);
		}
		this.setHttpMethod(HttpMethod.PUT);
	}

	@Override
	protected void validateParams() throws IllegalArgumentException {
		if (StringUtils.isBlank(this.getBucketname()))
			throw notNull("bucketname");
		if (StringUtils.isBlank(this.getObjectkey()))
			throw notNull("objectkey");
		if (file == null && this.getRequestBody() == null)
			throw notNull("file", "inputStream");
		if (this.redirectLocation != null) {
			if (!this.redirectLocation.startsWith("/")
					&& !this.redirectLocation.startsWith("http://")
					&& !this.redirectLocation.startsWith("https://"))
				throw notCorrect("redirectLocation", this.redirectLocation,
						"以/ http:// 或 https://开头");
		}
		if (file != null) {
			if (file.length() > Constants.maxSingleUpload)
				throw between("上传文件的大小", String.valueOf(file.length()),
						String.valueOf(0),
						String.valueOf(Constants.maxSingleUpload));
		} else {
			if (this.objectMeta != null
					&& this.objectMeta.getContentLength() > Constants.maxSingleUpload)
				throw between("Content-Length",
						String.valueOf(objectMeta.getContentLength()),
						String.valueOf(0),
						String.valueOf(Constants.maxSingleUpload));
		}
		if (this.callBackConfiguration != null) {
			if (StringUtils
					.isBlank(this.callBackConfiguration.getCallBackUrl())) {
				throw notNull("callBackConfiguration.callBackUrl");
			}
		}
		if (fops != null && fops.size() > 0) {
			for (Adp fop : fops) {
				if (StringUtils.isBlank(fop.getCommand())) {
					throw notNullInCondition("fops.command", "fops不为空");
				}
			}
			if (StringUtils.isBlank(notifyURL))
				throw notNullInCondition("notifyURL", "fops不为空");
		}
	}

	public File getFile() {
		return file;
	}

	private void setFile(File file) {
		this.file = file;
	}

	public ObjectMetadata getObjectMeta() {
		return objectMeta;
	}

	public void setObjectMeta(ObjectMetadata objectMeta) {
		this.objectMeta = objectMeta;
	}

	public CannedAccessControlList getCannedAcl() {
		return cannedAcl;
	}

	public void setCannedAcl(CannedAccessControlList cannedAcl) {
		this.cannedAcl = cannedAcl;
	}

	public AccessControlList getAcl() {
		return acl;
	}

	public void setAcl(AccessControlList acl) {
		this.acl = acl;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	public void setRedirectLocation(String redirectLocation) {
		this.redirectLocation = redirectLocation;
	}

	public CallBackConfiguration getCallBackConfiguration() {
		return callBackConfiguration;
	}

	public void setCallBackConfiguration(
			CallBackConfiguration callBackConfiguration) {
		this.callBackConfiguration = callBackConfiguration;
	}

	public List<Adp> getFops() {
		return fops;
	}

	public void setFops(List<Adp> fops) {
		this.fops = fops;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public String getMd5() {
		if (!StringUtils.isBlank(this.getContentMD5()))
			return this.getContentMD5();
		else
			return com.ksyun.ks3.utils.Base64
					.encodeAsString(((MD5DigestCalculatingInputStream) super
							.getRequestBody()).getMd5Digest());
	}
}
