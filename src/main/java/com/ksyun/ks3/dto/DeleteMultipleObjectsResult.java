package com.ksyun.ks3.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月10日 下午2:56:45
 * 
 * @description 
 **/
public class DeleteMultipleObjectsResult {
	private List<String> deleted = new ArrayList<String>();
	private List<DeleteMultipleObjectsError> errors = new ArrayList<DeleteMultipleObjectsError>();
	public List<String> getDeleted() {
		return deleted;
	}
	public void addDelete(String deleted) {
		this.deleted.add(deleted);
	}
	public List<DeleteMultipleObjectsError> getErrors() {
		return errors;
	}
	public void addError(DeleteMultipleObjectsError error) {
		this.errors.add(error);
	}
	public String toString()
	{
		return "DeleteMultipleObjectsResult[deleted="+this.deleted+",error="+this.errors+"]";
	}
}
