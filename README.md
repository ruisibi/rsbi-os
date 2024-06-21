# 睿思BI开源版-后端系统

- zh_CN [简体中文](README.md)
- en [English](README_en.md)

睿思BI开源版后端系统，基于Springboot构建，采用sqlite数据库，直接运行com.ruisitech.bi.RsbiOsApplication启动系统。<br/>
⚠️ 此项目前端地址：https://github.com/ruisibi/rsbi-vue ⚠️<br/>

“睿思BI”商业智能系统是由[成都睿思商智科技有限公司](https://www.ruisitech.com)自主研发的企业数据分析系统。 开源版包含数据建模、数据报表、多维分析，权限管理等功能模块，方便用户快速建立一套易用，灵活、免费的数据平台，实现数据的快速分析及可视化。 <br>

 快速开始：https://blog.csdn.net/zhuifengsn/article/details/138605460 <br>

# 产品数据库配置：<br>
sqlite:
```yaml
spring:
  datasource:
    url: jdbc:sqlite::resource:rsreport.sqlite3
    username:
    password:
    driver-class-name: org.sqlite.JDBC
pagehelper:
  helperDialect: sqlite
```
public/ext2/ext-config/ext-config.xml文件:
```xml
<!--修改支撑库为sqlite -->
<constant name="dbName" value="sqlite" />
```
mysql:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rs_report?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8
    username: root
    password: 12345678
    driver-class-name: com.mysql.cj.jdbc.Driver
```
public/ext2/ext-config/ext-config.xml文件:
```xml
<!--修改支撑库为mysql -->
<constant name="dbName" value="mysql" />
```
mysql的备份文件在datas目录下，在mysql创建 rs_report数据库后，把数据库文件还原到rs_report数据库中。

# 产品特点：<br>
1.轻量级BI, 支持快速建模，快速可视化数据。 <br> 
2.多维分析功能强大，支持下钻/上卷/排序/筛选/计算/聚合等多种操作方式。<br>
3.报表使用简单，功能强大，通过拖放等方式构建分析界面, 0代码编写。 <br>
4.开放源码，采用apache2.0开源协议，用户可任意使用而不需我公司授权（企业版除外）。<br>
  
# 系统功能：<br>
1.数据建模 (支持：mysql/oracle/sqlserver/db2/postgresql/hive/kylin) <br>
2.多维分析 <br>
3.数据报表 <br>
4.权限管理  <br>

# 技术支持：<br/>
请加QQ群 648548832, 此群为睿思bi技术交流。<br/>
<p/>

文档地址： http://www.ruisibi.cn/book.htm <br/>
演示地址： http://bi.ruisitech.com/  <br/>
<p/>

# 产品截图：<br/>
数据建模<br/>
![olap](https://www.ruisitech.com/img/kybpic0.jpg?v4)  <br/>
多维分析<br/>
![1](https://www.ruisitech.com/img/kybpic1.jpg?v5)  <br/>
数据报表<br/>
![2](https://www.ruisitech.com/img/kybpic2.jpg?v3)  <br/>
