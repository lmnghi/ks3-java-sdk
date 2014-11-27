package com.ksyun.ks3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月30日 下午12:06:48
 * 
 * @description
 **/
public class UploadPartTime {
	public static void print(int i, long t) throws IOException {
		FileWriter fileWriter = new FileWriter("D://time.txt", true);
		fileWriter.append("part:" + i + ",time:" + t+"\n");
		fileWriter.close();
	}
}
