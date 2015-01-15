package com.ksyun.ks3.service.multipartpost;

import java.io.Serializable;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年1月14日 下午5:47:07
 * 
 * @description 
 **/
public class UploadFileItem implements Serializable{
	private static final long serialVersionUID = 1L;

	// The form field name in a form used foruploading a file,

	// such as "upload1" in "<inputtype="file" name="upload1"/>"

	private String formFieldName;

	// File name to be uploaded, thefileName contains path,

	// such as "E:\\some_file.jpg"

	private String fileName;

	public UploadFileItem(String formFieldName, String fileName)

	{

		this.formFieldName = formFieldName;

		this.fileName = fileName;

	}

	public String getFormFieldName()

	{

		return formFieldName;

	}

	public void setFormFieldName(String formFieldName)

	{

		this.formFieldName = formFieldName;

	}

	public String getFileName()

	{

		return fileName;

	}

	public void setFileName(String fileName)

	{

		this.fileName = fileName;

	}
}