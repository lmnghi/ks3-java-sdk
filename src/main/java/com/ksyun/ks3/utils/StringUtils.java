package com.ksyun.ks3.utils;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ksyun.ks3.dto.Owner;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
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

	public static String object2string(Object obj) {
		return object2string(0, obj,null);
	}

	private static List<Class<?>> clazzs = Arrays.asList(new Class<?>[] {
			String.class, Boolean.class, Integer.class, Long.class,
			Double.class, Float.class, Short.class, Byte.class,
			Collection.class ,Map.class,HashMap.class,ArrayList.class,HashSet.class,java.util.Date.class});

	private static String object2string(int index, Object obj,Field fieldF) {
		StringBuffer value = new StringBuffer();
		StringBuffer prefixSb = new StringBuffer();
		for (int i = 0; i < index-1; i++) {
			prefixSb.append("       ");
		}
		String prefix = prefixSb.toString();
		if(fieldF!=null)
		    value.append("\n"+prefix+fieldF.getName()+"="+obj.getClass() + "\n");
		else
		    value.append("\n"+prefix+obj.getClass() + "\n");
		if(index!=0)
	    	prefixSb.append("       ");
		prefix = prefixSb.toString();
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<Field,Object> valuesToAdd = new HashMap<Field,Object>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			Object fieldValue = null;
			try {
				fieldValue = field.get(obj);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (fieldValue != null) {
				if (clazzs.contains(fieldValue.getClass())) {

					value.append(prefix+field.getName() + "=" + fieldValue.toString()
							+ "\n");
				} else if(fieldValue.getClass().isEnum()){
					value.append(prefix+field.getName() + "=" + fieldValue.toString()
							+ "\n");
				}
				else {
					valuesToAdd.put(field,fieldValue);
				}
			} else if(fieldValue==null){
				value.append(prefix+field.getName() + "=null" + "\n");
			}
		}
		for(Entry<Field,Object> obj1:valuesToAdd.entrySet()){
		    value.append(object2string(index + 1, obj1.getValue(),obj1.getKey()));
		}
		return value.toString();
	}
	public static boolean checkLong(Object o){
		String value = String.valueOf(o);
		Pattern pattern = Pattern.compile("^[0-9]+$");
		Matcher matcher = pattern.matcher(value);
		if(matcher.find())
			return true;
		return false;
	}
	public static String object2json(Object obj){
		return object2json(obj,false);
	}
	private static String object2json(Object obj,boolean escape){
		StringBuffer buffer = new StringBuffer();
		if(obj instanceof Map){
			buffer.append("{");
			Map<Object,Object> map =(Map)obj;
			int size = map.size();
			int count = 0;
			for(Entry entry :map.entrySet()){
				buffer.append("\""+escape(entry.getKey(),false)+"\""+":"+object2json(entry.getValue(),true));
				if(count<size-1)
					buffer.append(",");
				count++;
			}
			buffer.append("}");
		}else if(obj instanceof Collection){
			buffer.append("[");
			Collection<Object> collect = (Collection)obj;
			int size = collect.size();
			int count = 0;
			for(Object o :collect){
				if(count==2)
					buffer.append(object2json(o,true));
				else
					buffer.append(object2json(o));
				if(count<size-1)
					buffer.append(",");
				count++;
			}
			buffer.append("]");
		}else{
			if(escape)
				buffer.append("\""+escape(obj.toString(),true)+"\"");
			else
				buffer.append("\""+escape(obj.toString(),false)+"\"");
		}
		return buffer.toString();
	}
	private static List<Character> need =Arrays.asList(new Character[]{'\\','\"','$','\''});
	private static String escape(Object obj,boolean dollar){
		String s = obj.toString();
		byte [] chars = s.getBytes();
		int count = 0;
		for(int i = 0;i<chars.length;i++){
			if(need.contains((char)chars[i])&&(dollar||(char)chars[i]!='$')){
				count ++;
			}
		}
		byte [] newChars = new byte[chars.length+count]; 
		for(int i = 0, j = 0;i < chars.length;i++){
			if(need.contains((char)chars[i])&&(dollar||(char)chars[i]!='$')){
				newChars[i+j] = '\\';
				newChars[i+j+1] = chars[i];
				j++;
			}else{
				newChars[i+j] = chars[i];
			}
		}
		return new String(newChars);
	}
}
