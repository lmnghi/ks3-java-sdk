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

sdk使用说明