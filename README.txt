概念和术语

AccessKeyID、AccessKeySecret
使用KS3，您需要KS3颁发给您的AccessKeyID（长度为20个字符的ASCII字符串）和AccessKeySecret（长度为40个字符的ASCII字符串）。AccessKeyID用于标识客户的身份，AccessKeySecret作为私钥形式存放于客户服务器不在网络中传递。AccessKeySecret通常用作计算请求签名的密钥，用以保证该请求是来自指定的客户。使用AccessKeyID进行身份识别，加上AccessKeySecret进行数字签名，即可完成应用接入与认证授权。
Object
在KS3中，用户操作的基本数据单元是Object。单个Object允许存储0~1TB的数据。 Object 包含key和data。其中，key是Object的名字；data是Object 的数据。key为UTF-8编码，且编码后的长度不得超过1024个字节。
Bucket
Bucket是存放Object的容器，所有的Object都必须存放在特定的Bucket中。每个用户最多可以创建20个Bucket，每个Bucket中可以存放无限多个Object。Bucket不能嵌套，每个Bucket中只能存放Object，不能再存放Bucket，Bucket下的Object是一个平级的结构。Bucket的名称全局唯一且命名规则与DNS命名规则相同：
仅包含小写英文字母（a-z），数字，点（.），中线，即： abcdefghijklmnopqrstuvwxyz0123456789.-
必须由字母或数字开头
长度在3和255个字符之间
不能是IP的形式，类似192.168.0.1
不能以kss开头
Service
KS3提供给用户的虚拟存储空间，在这个虚拟空间中，每个用户可拥有一个到多个Bucket。
ACL
对Bucket和Object相关访问的控制策略，例如允许匿名用户公开访问等。 目前ACL支持{READ, WRITE, FULL_CONTROL}三种权限。 目前，对于bucket的拥有者，总是FULL_CONTROL。可以设置匿名用户为READ， WRITE, 或者FULL_CONTROL权限。 对于BUCKET来说，READ是指罗列bucket中文件的功能。WRITE是指可以上传，删除BUCKET中文件的功能。FULL_CONTROL则包含所有操作。 对于OBJECT来说，READ是指查看或者下载文件的功能。WRITE无意义。FULL_CONTROL则包含所有操作。
Logging
对Bucket和Object的日志配置。

错误码列表

message	HTTP Status	描述
AccessDenied	403	拒绝访问
BadDigest	400	错误的摘要
BucketAlreadyExists	409	Bucket已经存在
BucketAlreadyOwnedByYou	409	用户已经是Bucket的拥有者
BucketNotEmpty	409	Bucket不为空
InternalError	500	内部错误
InvalidAccessKeyId	403	无效的AccessKeyId
InvalidACLString	400	ACL配置无效
InvalidAuthorizationString	400	无效的验证字符串
InvalidBucketName	400	无效的Bucket名称
InvalidDateFormat	400	无效的日期格式
InvalidDigest	400	无效的摘要
InvalidEncryptionAlgorithm	400	无效的指定加密算法
InvalidHostHeader	400	无效的头信息
InvalidParameter	400	无效的参数
InvalidPath	400	无效的路径
InvalidQueryString	400	无效的请求字符串
InvalidRange	416	无效的range
KeyTooLong	400	Key太长
MetadataTooLarge	400	metadata过大
MethodNotAllowed	405	不支持的方法
MissingDateHeader	400	头信息中缺少data
MissingHostHeader	400	头信息中缺少host
NoSuchBucket	404	该Bucket不存在
NoSuchKey	404	该Key不存在
NotImplemented	501	无法处理的方法
RequestTimeTooSkewed	403	发起请求的时间和服务器时间超出15分钟
SignatureDoesNotMatch	403	签名不匹配
TooManyBuckets	400	用户的Bucket数目超过限制
URLExpired	403	url过期
BadParams	400	参数错误
ImageTypeNotSupport	400	图片类型不支持
MissingFormArgs	400	没有上传Policy
ContentRangeError	400	Range错误
ContentLengthOutOfRange	400	上传文件内容大于range
PolicyError	400	Policy错误
ExpirationError	400	Policy中没有expiration
FormUnmatchPolicy	400	表单中的内容和policy不匹配

详见 http://ks3.ksyun.com/doc/api/index.html