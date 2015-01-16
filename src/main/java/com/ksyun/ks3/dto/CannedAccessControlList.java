package com.ksyun.ks3.dto;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2014年10月22日 上午10:36:46
 * 
 * @description 对ks3 bucket或object的{@link AccessControlList}的一种快捷配置方式
 * <p>有Private,私有；PublicRead,公开读;PublicReadWrite公开读写三种</p>
 **/
public enum CannedAccessControlList {
	/**
	 * 私有
	 */
	Private("private"),
	/**
	 * 公开读
	 */
	PublicRead("public-read"), 
	/**
	 * 公开读写(对object而言，公开写是无意义的)
	 */
	PublicReadWrite("public-read-write");
	private final String cannedAclHeader;

	private CannedAccessControlList(String cannedAclHeader) {
		this.cannedAclHeader = cannedAclHeader;
	}
	public String toString() {
		return cannedAclHeader;
	}
}
