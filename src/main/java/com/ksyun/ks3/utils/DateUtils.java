package com.ksyun.ks3.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.ksyun.ks3.exception.Ks3ClientException;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月15日 下午2:53:35
 * 
 * @description
 **/
public class DateUtils {
	public static enum DATETIME_PROTOCOL {
		RFC1123, ISO8861;
	};

	public static Date convertStr2Date(String datetimeTxt,
			DATETIME_PROTOCOL protocol) {

		if (protocol.equals(DATETIME_PROTOCOL.RFC1123)) {
			DateTimeFormatter RFC1123_DATE_TIME_FORMATTER = DateTimeFormat
					.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
					.withZoneUTC().withLocale(Locale.ENGLISH);
			DateTime dt = RFC1123_DATE_TIME_FORMATTER
					.parseDateTime(datetimeTxt);
			return dt.toDate();
		} else if (protocol.equals(DATETIME_PROTOCOL.ISO8861)) {

			DateTimeFormatter ISO8861_FORMATTER = ISODateTimeFormat.dateTime()
					.withZoneUTC();
			DateTime dt = ISO8861_FORMATTER.parseDateTime(datetimeTxt);
			return dt.toDate();
		}
		return null;
	}

	public static Date convertStr2Date(String datetimeText, SimpleDateFormat sdf) {
		try {
			return sdf.parse(datetimeText);
		} catch (ParseException e) {
			throw new Ks3ClientException(
					"The server did not return the expected value,it is "
							+ datetimeText, e);
		}
	}

	public static Date convertStr2Date(String datetimeText) {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss z", Locale.UK);
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		if (datetimeText.endsWith("Z")) {
			datetimeText = datetimeText.replace("Z", " GMT");
			sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z");
		}
		try {
			return sdf.parse(datetimeText);
		} catch (ParseException e) {
			throw new Ks3ClientException(
					"The server did not return the expected value,it is "
							+ datetimeText, e);
		}

	}

	public static String convertDate2Str(Date date, DATETIME_PROTOCOL protocol) {
		if (protocol.equals(DATETIME_PROTOCOL.RFC1123)) {

			DateTimeFormatter RFC1123_DATE_TIME_FORMATTER = DateTimeFormat
					.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'")
					.withZoneUTC().withLocale(Locale.ENGLISH);

			return RFC1123_DATE_TIME_FORMATTER.print(date.getTime());

		} else if (protocol.equals(DATETIME_PROTOCOL.ISO8861)) {

			DateTimeFormatter ISO8861_FORMATTER = ISODateTimeFormat.dateTime()
					.withZoneUTC();
			return ISO8861_FORMATTER.print(date.getTime());
		}
		return null;
	}
}
