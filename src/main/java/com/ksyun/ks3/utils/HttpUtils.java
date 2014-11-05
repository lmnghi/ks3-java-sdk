package com.ksyun.ks3.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.ksyun.ks3.dto.AccessControlList;
import com.ksyun.ks3.dto.AccessControlPolicy;
import com.ksyun.ks3.dto.Grant;
import com.ksyun.ks3.dto.Permission;
import com.ksyun.ks3.http.HttpHeaders;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月17日 上午10:23:53
 * 
 * @description
 **/
public class HttpUtils {
	private static final Log log = LogFactory.getLog(HttpUtils.class);
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final Pattern ENCODED_CHARACTERS_PATTERN;
	static {
		StringBuilder pattern = new StringBuilder();

		pattern.append(Pattern.quote("+")).append("|")
				.append(Pattern.quote("*")).append("|")
				.append(Pattern.quote("%7E")).append("|")
				.append(Pattern.quote("%2F"));

		ENCODED_CHARACTERS_PATTERN = Pattern.compile(pattern.toString());
	}

	public static void printHttpRequest(HttpRequestBase request) {
		log.info("the http request info:");
		for (Header s : request.getAllHeaders()) {
			log.info("headers:" + s.getName() + " " + s.getValue());
		}
		log.info("requestline:" + request.getRequestLine().getMethod() + " "
				+ request.getRequestLine().getUri() + " "
				+ request.getRequestLine().getProtocolVersion());
	}

	
	public static Map<String,String> convertAcl2Headers(AccessControlList acl)
	{
		Map<String,String> headers = new HashMap<String,String>();
		
		List<String> grants_fullcontrol= new ArrayList<String>();
		List<String> grants_read= new ArrayList<String>();
		List<String> grants_write= new ArrayList<String>();
		for(Grant grant:acl.getGrants())
		{
			if(grant.getPermission().equals(Permission.FullControl))
			{
				grants_fullcontrol.add(grant.getGrantee().getTypeIdentifier()+"=\""+grant.getGrantee().getIdentifier()+"\"");
			}
			else if(grant.getPermission().equals(Permission.Read))
			{
				grants_read.add(grant.getGrantee().getTypeIdentifier()+"=\""+grant.getGrantee().getIdentifier()+"\"");
			}
			else if(grant.getPermission().equals(Permission.Write))
			{
				grants_write.add(grant.getGrantee().getTypeIdentifier()+"=\""+grant.getGrantee().getIdentifier()+"\"");
			}
		}
		if(grants_fullcontrol.size()>0)
		{
			headers.put(HttpHeaders.GrantFullControl.toString(),StringUtils.join(grants_fullcontrol,","));
		}
		if(grants_read.size()>0)
		{
			headers.put(HttpHeaders.GrantRead.toString(),StringUtils.join(grants_read,","));
		}
		if(grants_write.size()>0)
		{
			headers.put(HttpHeaders.GrantWrite.toString(),StringUtils.join(grants_write,","));
		}
		return headers;
	}
	
	public static String urlEncode(final String value, final boolean path) {
		if (value == null) {
			return "";
		}

		try {
			String encoded = URLEncoder.encode(value, DEFAULT_ENCODING);

			Matcher matcher = ENCODED_CHARACTERS_PATTERN.matcher(encoded);
			StringBuffer buffer = new StringBuffer(encoded.length());

			while (matcher.find()) {
				String replacement = matcher.group(0);

				if ("+".equals(replacement)) {
					replacement = "%20";
				} else if ("*".equals(replacement)) {
					replacement = "%2A";
				} else if ("%7E".equals(replacement)) {
					replacement = "~";
				} else if (path && "%2F".equals(replacement)) {
					replacement = "/";
				}

				matcher.appendReplacement(buffer, replacement);
			}

			matcher.appendTail(buffer);
			return buffer.toString();

		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}
}
