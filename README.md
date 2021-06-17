
QQ付费入群系统Mirai机器人插件
===============
## 使用：


1. 使用Idea打开项目，打开项目，找到File-Project Structrue  
使用AdoptOpenJDK-11，请勿使用其他版本JDK（如Oracle OpenJDK）
   
![image](https://user-images.githubusercontent.com/32336368/122315666-14016800-cf4d-11eb-94eb-0942d1548a8a.png)

2. 修改源码内的机器人信息

```java
//数据库连接信息
static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/fk";
static final String USER = "fk";
static final String PASS = "password";

//拒绝申请理由
static final String REJECT_STRING = "请在xxx.xxx.com支付后入群";
```  

3. 开始编译jar插件  

![image](https://user-images.githubusercontent.com/32336368/121608601-ea47cd00-ca84-11eb-8e27-82a4720d5f1d.png)  

4. 将jar插件放到mirai-console运行  
   
![image](https://user-images.githubusercontent.com/32336368/121608982-7f4ac600-ca85-11eb-8e0f-7fc680bf5d41.png)  
   
5. done
------

## 注意：

+ QQ付费入群系统是免费开源产品，所有程序均开放源代码，所以不会有收费计划，因此作者不可能教会每个人部署安装，请参考文档多百度谷歌，使用具有一定的技术门槛，请见谅！

## 版权信息

QQ付费入群系统遵循 MIT License 开源协议发布，并提供免费使用，请勿用于非法用途。