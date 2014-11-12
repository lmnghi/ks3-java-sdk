package com.ksyun.ks3.utils;

import java.util.List;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午8:06:43
 * 
 * @description
 **/
public class StringUtils {
	public static final int MIN_BUCKET_NAME_LENGTH = 3;
	public static final int MAX_BUCKET_NAME_LENGTH = 63;

	public static String join(Object[] strings, String spliter) {
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		for (Object s : strings) {
			if (i == 0) {
				buffer.append(s);
				i = 1;
			} else {
				buffer.append(spliter + s);
			}
		}
		return buffer.toString();
	}
	public static String join(int[] strings, String spliter) {
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		for (Object s : strings) {
			if (i == 0) {
				buffer.append(s);
				i = 1;
			} else {
				buffer.append(spliter + s);
			}
		}
		return buffer.toString();
	}
	public static String join(byte[] strings, String spliter) {
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		for (Object s : strings) {
			if (i == 0) {
				buffer.append(s);
				i = 1;
			} else {
				buffer.append(spliter + s);
			}
		}
		return buffer.toString();
	}
	public static String join(List<String> strings, String spliter) {
		return join(strings.toArray(), spliter);
	}
	public static boolean isBlank(String s) {
		if (s == null)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public static String validateBucketName(String bname) {
		if (bname == null) {
			return null;
		}

		if (bname.length() < MIN_BUCKET_NAME_LENGTH
				|| bname.length() > MAX_BUCKET_NAME_LENGTH) {
			return null;
		}

		char previous = '\0';

		for (int i = 0; i < bname.length(); ++i) {
			char next = bname.charAt(i);

			if (next >= 'A' && next <= 'Z') {
				return null;
			}

			if (next == ' ' || next == '\t' || next == '\r' || next == '\n') {
				return null;
			}

			if (next == '.') {
				if (previous == '.') {
					return null;
				}
				if (previous == '-') {
					return null;
				}
			} else if (next == '-') {
				if (previous == '.') {
					return null;
				}
			} else if ((next < '0') || (next > '9' && next < 'a')
					|| (next > 'z')) {
				return null;
			}

			previous = next;
		}
		if (previous == '.' || previous == '-') {
			return null;
		}
		return bname;
	}

}
