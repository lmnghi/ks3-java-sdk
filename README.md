#KS3 SDK For Java使用指南
##1 简介
###1.1 金山标志存储服务
金山标准存储服务（Kingsoft Standard Storage Service），简称KS3，是金山云为开发者提供无限制、多备份、分布式的低成本存储空间解决方案。目前提供多种语言SDK，替开发者解决存储扩容、数据可靠安全以及分布式访问等相关复杂问题，开发者可以快速的开发出涉及存储业务的程序或服务。  
###1.2 概念和术语
AccessKeyID、AccessKeySecret  
使用KS3，您需要KS3颁发给您的AccessKeyID（长度为20个字符的ASCII字符串）和AccessKeySecret（长度为40个字符的ASCII字符串）。AccessKeyID用于标识客户的身份，AccessKeySecret作为私钥形式存放于客户服务器不在网络中传递。AccessKeySecret通常用作计算请求签名的密钥，用以保证该请求是来自指定的客户。使用AccessKeyID进行身份识别，加上AccessKeySecret进行数字签名，即可完成应用接入与认证授权。  
Object  
在KS3中，用户操作的基本数据单元是Object。  
Bucket  
Bucket是存放Object的容器，所有的Object都必须存放在特定的Bucket中。  
Service  
KS3提供给用户的虚拟存储空间，在这个虚拟空间中，每个用户可拥有一个到多个Bucket。  
ACL  
对Bucket和Object相关访问的控制策略，例如允许匿名用户公开访问等。 目前ACL支持{READ, WRITE, FULLCONTROL}三种权限。 目前，对于bucket的拥有者，总是FULLCONTROL。可以设置匿名用户为READ， WRITE, 或者FULLCONTROL权限。 对于BUCKET来说，READ是指罗列bucket中文件的功能。WRITE是指可以上传，删除BUCKET中文件的功能。FULLCONTROL则包含所有操作。 对于OBJECT来说，READ是指查看或者下载文件的功能。WRITE无意义。FULLCONTROL则包含所有操作。  
Logging  
对Bucket和Object的日志配置。  
###1.3 KS3 SDK For Java
此SDK适用于Java 5及以上版本。基于KS3 API 构建。使用此 SDK 构建您的网络应用程序，能让您以非常便捷地方式将数据安全地存储到七牛云存储上。无论您的网络应用是一个网站程序，还是包括从云端（服务端程序）到终端（手持设备应用）的架构的服务或应用，通过KS3存储及其 SDK，都能让您应用程序的终端用户高速上传和下载，同时也让您的服务端更加轻盈。  
com.ksyun.ks3:几个在上传时用到的特殊的流  
com.ksyun.ks3.config:sdk客户端配置  
com.ksyun.ks3.dto:数据传输对象  
com.ksyun.ks3.exception:异常  
com.ksyun.ks3.exception.serviceside:ks3服务端的异常  
com.ksyun.ks3.http:http相关内容及对Apache Http Client的封装  
com.ksyun.ks3.service:Ks3客户端，所有的对API的操作都是在这里进行的  
com.ksyun.ks3.service.request:对API请求时参数的封装，用户进行使用时首先应该实例化并配置一个request然后通过    com.ksyun.ks3.service包下的客户端进行操作  
com.ksyun.ks3.service.response:对请求API返回的结果的解析器  
com.ksyun.ks3.signer:签名生成器，具体使用哪个签名生成器可以在ClientConfig中配置(一般情况下请勿修改)  
com.ksyun.ks3.utils:工具包  
##2 环境准备
配置Java 5 以上开发环境  
下载KS3 SDK For Java  
添加Maven依赖

    <dependency>
        <groupId>com.ksyun.ks3</groupId>
        <artifactId>ks3-kss-java-sdk</artifactId>
        <version>0.0.1</version>
    </dependency>
    
##3 初始化
###3.1 配置参数
####3.1.1 配置方法一

	ClientConfig config = ClientConfig.getConfig();
	config.set(ClientConfig.CONNECTION_TIMEOUT, "50000");//连接超时时间
	config.set(ClientConfig.SOCKET_TIMEOUT,"50000");//socket超时时间
	config.set(ClientConfig.SOCKET_SEND_BUFFER_SIZE_HINT,"8192");//socket发送数据时buffer大小
	config.set(ClientConfig.SOCKET_RECEIVE_BUFFER_SIZE_HINT,"8192");//socket接受数据时buffer大小
	config.set(ClientConfig.MAX_RETRY,"5");//请求超时或发送错误时重试次数
	config.set(ClientConfig.CONNECTION_TTL,"-1");//连接池中请求的过期时间。当请求需要重试时，使用这个参数检测是否可用。
	config.set(ClientConfig.MAX_CONNECTIONS,"50");//连接池最大连接数
	config.set(ClientConfig.PROXY_HOST,null);//代理host
	config.set(ClientConfig.PROXY_PORT,"-1");//代理端口
	config.set(ClientConfig.PROXY_DAMAIN,null);//Windows domain name for configuration an NTLM proxy
	config.set(ClientConfig.PROXY_PASSWORD,null);//代理用户名
	config.set(ClientConfig.PROXY_USER_NAME,null);//代理密码
	config.set(ClientConfig.PROXY_WORKSTATION,null);//Windows workstation name for configuring NTLM proxy
	config.set(ClientConfig.IS_PREEMPTIVE_BASIC_PROXY_AUTH,"false");//Whether to authenticate preemptively against proxy server.
	config.set(ClientConfig.END_POINT,"kss.ksyun.com");//KS3服务器域名
	config.set(ClientConfig.CLIENT_SIGNER,"com.ksyun.ks3.signer.DefaultSigner");//配置签名算法生成器，一般情况下请勿修改。
####3.1.2配置方法二
新建配置文件config.properties

	httpclient.connectionTimeout=50000//连接超时时间
	httpclient.socketTimeout=50000//socket超时时间
	httpclient.socketSendBufferSizeHint=8192//socket发送数据时buffer大小
	httpclient.socketReceiveBufferSzieHint=8192//socket接受数据时buffer大小
	httpclient.maxRetry=5//请求超时或发送错误时重试次数
	httpclient.connnetionTTL=-1//连接池中请求的过期时间。当请求需要重试时，使用这个参数检测是否可用。
	httpclient.maxConnections=50//连接池最大连接数
	httpclient.proxyHost=null//代理host
	httpclient.proxyPort=-1//代理端口
	httpclient.ProxyUserName=null//代理用户名
	httpclient.ProxyPassword=null//代理密码
	httpclient.ProxyDomain=null//Windows domain name for configuration an NTLM proxy
	httpclient.ProxyWorkStation=null//Windows workstation name for configuring NTLM proxy
	httpclient.isPreemptiveBasicProxyAuth=false//Whether to authenticate 
	ks3client.endpoint=kss.ksyun.com//KS3服务器域名
	ks3client.signer=com.ksyun.ks3.signer.DefaultSigner//配置签名算法生成器，一般情况下请勿修改。

新建类
	
	public class DemoConfigLoader implements ConfigLoader{
		public ClientConfig load(ClientConfig config) {
			依次循环调用config.set(String key, String value)方法将配置文件中的键值对设置进去
			return config;
		}
	}

在代码初始化时

	ClientConfig.addConfigLoader(new DemoConfigLoader());
###3.2 配置日志
该SDK使用log4j，请用户自行配置log4j.properties
###3.3 获取秘钥
1、开通KS3服务，[http://www.ksyun.com/user/register](http://www.ksyun.com/user/register) 注册账号  
2、进入控制台,[http://ks3.ksyun.com/console.html#/setting]http://ks3.ksyun.com/console.html#/setting获取AccessKeyID 、AccessKeySecret
###3.4 初始化客户端
当以上全部完成之后用户便可初始化客户端进行操作了  

	Ks3 client = new Ks3Client("<您的AccessKeyID>","<您的AccessKeySecret>");
##4 公共异常说明
注意:以下异常全部继承自Ks3ServiceException(Ks3ServiceException继承自RuntimeException)
<table>
<tr>
<th>异常</th>
<th>说明</th>
</tr>
<tr>
<td>SignatureDoesNotMatchException</td>
<td>用户签名验证未能验证通过</td>
</tr>
<tr>
<td>InvalidAccessKeyIdException</td>
<td>用户提供的AccessKeyId不存在</td>
</tr>
<tr>
<td>RequestTimeTooSkewedException</td>
<td>客户端发送请求的时间和服务端接收到请求的时间差距过大</td>
</tr>
<tr>
<td>AccessDeniedException</td>
<td>用户没有权限访问当前资源</td>
</tr>
<tr>
<td>NoSuchBucketException</td>
<td>若用户请求中包含Bucket，但是该bucket在服务器上不存在</td>
</tr>
<tr>
<td>NoSuchKeyException</td>
<td>若用户请求中包含Bucket和Object Key，但是该object Key在对应的Bucket中不存在</td>
</tr>
<tr>
<td>NotImplementedException</td>
<td>您提供的一个功能性的Header尚未实现，正常使用SDK时不应该抛出</td>
</tr>
<tr>
<td>NotFoundApiException</td>
<td>不存在这个API，正常使用SDK时不应该抛出</td>
</tr>
<tr>
<td>URLExpiredException</td>
<td>这个URL已经过期</td>
</tr>
<tr>
<td>InternalErrorException</td>
<td>服务器内部错误，稍后再试或联系我们</td>
</tr>
</table>
##5 使用示例
###5.1 Service接口
####5.1.1 GET Service(List Buckets)
#####5.1.1.1 使用示例
列出当前用户的所有bucket,可以查看每个bucket的名称、创建时间以及所有者

	public List<Bucket> listBuckets(){
		List<Bucket> buckets = client.listBuckets();
		return buckets;
	}
#####5.1.1.2 特殊异常
该方法不会抛出特殊异常
###5.2 Bucket接口
####5.1.2 DELETE Bucket
#####5.1.2.1 使用示例
删除一个Bucket

	public void deleteBucket(){
		client.deleteBucket("<您的bucket名称>");
	}
#####5.1.2.2 特殊异常
<table>
<tr>
<th>异常</th>
<th>说明</th>
</tr>
<tr>
<td>BucketNotEmptyException</td>
<td>这个bucket不为空，无法删除，需要用户先调用client.clearBucket(bucket)方法清空bucket</td>
</tr>
</table>
###5.3 Object接口
