package com.ksyun.ks3.http;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月15日 下午1:49:51
 * 
 * @description 
 **/
public enum HttpHeaders {
	RequestId("x-kss-request-id"),
	
	Authorization("Authorization"),
	Date("Date"),
	Host("Host"),
	Server("Server"),
	ExtendedRequestId("x-kss-id-2"),
	ContentMD5("Content-MD5"),
	Expect("Expect"),
	IfMatch("If-Match"),
	IfNoneMatch("If-None-Match"),
	IfModifiedSince("If-Modified-Since"),
	IfUnmodifiedSince("If-Unmodified-Since"),
	//put object metadata
	ContentLength("Content-Length"),
	CacheControl("Cache-Control"),
	ContentType("Content-Type"),
	ContentDisposition("Content-Disposition"),
	ContentEncoding("Content-Encoding"),
	Expires("Expires"),
	Range("Range"),
	//
	//acl
	CannedAcl("x-kss-acl"),
    AclPrivate("x-kss-acl-private"),
    AclPubicRead("x-kss-acl-public-read"),
    AclPublicReadWrite("x-kss-acl-public-write"),
    AclPublicAuthenticatedRead("x-kss-acl-public-authenticated-read"),
	GrantFullControl("x-kss-grant-full-control"),
	GrantRead("x-kss-grant-read"),
	GrantWrite("x-kss-grant-write"),
	//
	ServerSideEncryption("x-kss-server-side-encryption"),
	//
	ETag("ETag"),
	LastModified("Last-Modified"),
	//get object response
	/**
	 *default false
	 */
	XKssDeleteMarker("x-kss-delete-marker"),
	XKssExpiration("x-kss-expiration"),
	/**
	 * default AES256
	 */
	XKssServerSideEncryption("x-kss-server-side​-encryption"),
	/**
	 * default AES256
	 */
	XKssServerSideEncryptionCustomerAlgorithm("x-kss-server-side​-encryption​-customer-algorithm"),
	XkssServerSideEncryptionCustomerKeyMD5("x-kss-server-side​-encryption​-customer-key-MD5"),
	/**
	 * default None
	 */
	XKssRestore("x-kss-restore"),
	/**
	 * default None
	 */
	XKssWebsiteRedirectLocation("x-kss-website​-redirect-location")
	;
	private String value;
	
	HttpHeaders(String value)
	{
		this.value = value;
	}
	@Override
	public String toString()
	{
		return this.value;
	}
}
