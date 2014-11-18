概念和术语

AccessKeyID、AccessKeySecret
使用KS3，您需要KS3颁发给您的AccessKeyID（长度为20个字符的ASCII字符串）和AccessKeySecret（长度为40个字符的ASCII字符串）。AccessKeyID用于标识客户的身份，AccessKeySecret作为私钥形式存放于客户服务器不在网络中传递。AccessKeySecret通常用作计算请求签名的密钥，用以保证该请求是来自指定的客户。使用AccessKeyID进行身份识别，加上AccessKeySecret进行数字签名，即可完成应用接入与认证授权。
Object
在KS3中，用户操作的基本数据单元是Object。
Bucket
Bucket是存放Object的容器，所有的Object都必须存放在特定的Bucket中。
Service
KS3提供给用户的虚拟存储空间，在这个虚拟空间中，每个用户可拥有一个到多个Bucket。
ACL
对Bucket和Object相关访问的控制策略，例如允许匿名用户公开访问等。 目前ACL支持{READ, WRITE, FULL_CONTROL}三种权限。 目前，对于bucket的拥有者，总是FULL_CONTROL。可以设置匿名用户为READ， WRITE, 或者FULL_CONTROL权限。 对于BUCKET来说，READ是指罗列bucket中文件的功能。WRITE是指可以上传，删除BUCKET中文件的功能。FULL_CONTROL则包含所有操作。 对于OBJECT来说，READ是指查看或者下载文件的功能。WRITE无意义。FULL_CONTROL则包含所有操作。
Logging
对Bucket和Object的日志配置。

详见 http://ks3.ksyun.com/doc/api/index.html

sdk包

com.ksyun.ks3:几个在上传时用到的特殊的流
com.ksyun.ks3.config:sdk客户端配置
com.ksyun.ks3.dto:数据传输对象
com.ksyun.ks3.exception:异常
com.ksyun.ks3.exception.serviceside:ks3服务端的异常
com.ksyun.ks3.http:http相关内容及对Apache Http Client的封装
com.ksyun.ks3.service:Ks3客户端，所有的对API的操作都是在这里进行的
com.ksyun.ks3.service.request:对API请求时参数的封装，用户进行使用时首先应该实例化并配置一个request然后通过com.ksyun.ks3.service包下的客户端进行操作
com.ksyun.ks3.service.response:对请求API返回的结果的解析器
com.ksyun.ks3.signer:签名生成器，具体使用哪个签名生成器可以在ClientConfig中配置
com.ksyun.ks3.utils:工具包


sdk使用示例

//客户端初始化
final String accesskeyId = "accesskeyid";
final String accesskeySecret = "accesskeysecret";
Ks3 client = new Ks3Client(accesskeyId,accesskeySecret);
//初始化新建bucket请求
CreateBucketConfiguration config = new CreateBucketConfiguration(REGION.BEIJING);

CreateBucketRequest request = new CreateBucketRequest("example");
request.setCannedAcl(CannedAccessControlList.PublicRead);//设置bucket访问权限
request.setConfig(config);//设置bucket存储地点
Bucket bucket = client.createBucket(request);//执行操作