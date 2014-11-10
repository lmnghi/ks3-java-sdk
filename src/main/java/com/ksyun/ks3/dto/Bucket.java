package com.ksyun.ks3.dto;

import java.util.Date;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月14日 下午6:00:34
 * 
 * @description 存储空间
 **/
public class Bucket {
	private static final long serialVersionUID = -8646831898339939580L;

    private String name = null;
    private Owner owner = null;
    private Date creationDate = null;
    public Bucket() {}
    public Bucket(String name) {
        this.name = name;
    }
    public String toString() {
        return "S3Bucket [name=" + getName()
                + ", creationDate=" + getCreationDate()
                + ", owner=" + getOwner() + "]";
    }
    public int hashCode(){
    	return this.name.hashCode();
    }
    @Override
    public boolean equals(Object obj){
    	if(obj instanceof Bucket)
    	    return this.name.equals(((Bucket)obj).getName());
    	else
    		return false;
    }
    public Owner getOwner() {
        return owner;
    }
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
