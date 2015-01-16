package com.ksyun.ks3.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.InputStreamEntity;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月22日 下午3:10:40
 * 
 * @description
 **/
public class RepeatableInputStreamRequestEntity extends BasicHttpEntity {
	private boolean firstAttempt = true;

	private InputStreamEntity inputStreamRequestEntity;

	private InputStream content;

	private IOException originalException;

	public RepeatableInputStreamRequestEntity(InputStream content,String length) {
		setChunked(false);

		long contentLength = -1;
		try {
			String contentLengthString = length;
			if (contentLengthString != null) {
				contentLength = Long.parseLong(contentLengthString);
			}
		} catch (NumberFormatException nfe) {
			
		}

		inputStreamRequestEntity = new InputStreamEntity(content, contentLength);
		inputStreamRequestEntity.setContentType(contentType);
		this.content = content;

		setContent(content);
		setContentType(contentType);
		setContentLength(contentLength);
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public boolean isRepeatable() {
		return content.markSupported()
				|| inputStreamRequestEntity.isRepeatable();
	}

	@Override
	public void writeTo(OutputStream output) throws IOException {
		try {
			if (!firstAttempt && isRepeatable())
				content.reset();

			firstAttempt = false;
			inputStreamRequestEntity.writeTo(output);
		} catch (IOException ioe) {
			if (originalException == null)
				originalException = ioe;
			throw originalException;
		}
	}
}
