# 信息类

## BasicInfo

消息类基类，序列化

### 属性

序列化ID，这个是为了保证拥有不同方法的相同类，能够被顺利的序列化与反序列化

ID，这是整个系统中的、所有消息收发对象的、唯一身份识别信息
为了方便生成，采用BigInteger

Name，这是消息对象的公有属性，是必要的，默认为"Defualt_name"

### 方法

getID：获取ID
没有setID，ID一旦创建不可修改，且不能再被另外的对象使用（即使某用户或群聊已注销，占用的ID也不会被复用）

set与getName：获取、修改姓名

### 接口
不需要

## UserInfo

用户信息类


### 属性

E-mail
PhoneNumber

### 方法

get\set Email
get\set PhoneNumber

## GroupInfo

### 属性

NumberIDs 群成员ID，可能用Hash装

### 方法

addNumber 加入成员

deleteNumber 删除成员

searchNumber 查找成员

## AdministratorInfo

这个比较特殊，官方账号（用于群发消息以及处理客服消息的），以UserInfo为基础。
*要搞这个类的原因，其实是另一个设计的依赖：服务器管理端；如果服务器要在LinuxVps上部署，那么无法设计图形化服务端，因此只能设计一个服务器管理端，用于管理整个系统；而该管理端实质上也是一个客户端，只不过修改了部分功能；因此需要一个管理员信息。*