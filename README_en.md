# Ruisi BI Open Source Edition - Back-end System

- zh_CN [简体中文](README.md)
- en [English](README_en.md)

The back-end system of Ruisi BI open source version is built based on Springboot. It adopts sqlite database and directly runs the com.ruisitech.bi.RsbiOsApplication startup system.<br/>
⚠️ Front-end address of this project: https://github.com/ruisibi/rsbi-vue ⚠️<br/>

"Ruisi BI" business intelligence system is an enterprise data analysis system independently developed by [Chengdu Ruisi Business Intelligence Technology Co., Ltd.](https://www.ruisitech.com). The open source version includes data modelling, data reporting, multi-dimensional analysis, permission management and other functional modules, which is convenient for users to quickly establish a set of easy-to-use and flexible data analysis platforms to realise rapid analysis and visualisation of data. <br>

Quickly start: https://blog.csdn.net/zhuifengsn/article/details/138605460 <br>

# Product database configuration: <br>
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
The backup file of mysql is in the datas directory. After mysql creates the rs_report database, the database file is restored to the rs_report database.

# Product features: <br>
1.Lightweight BI supports rapid modelling and rapid visualisation of data. <br> 
2.Multi-dimensional analysis is powerful and supports a variety of operation methods such as drilling/upwinding/sorting/screening/calculation/aggregation.<br>
3.The report is simple to use and powerful. It builds an analysis interface through drag-and-drop methods, and writes 0 code. <br>
4.Open source code, using apache2.0 open source protocol, users can use it at will without our company's authorisation (except for the flagship version). <br>

# System function:<br>
1.Data modelling (supports：mysql/oracle/sqlserver/db2/postgresql/hive/kylin) <br>
2.Multidimensional analysis (OLAP) <br>
3.Data report <br>
4.Permission management  <br>

# Technical support:<br/>
Please add QQ group 648548832, this group is Ruisibi technology exchange.<br/>
<p/>

Document: http://www.ruisibi.cn/book.htm <br/>
Demonstrate： http://bi.ruisitech.com/  <br/>
<p/>

# Product screenshot:<br/>
Data modelling<br/>
![olap](https://www.ruisitech.com/img/3461099639.png?v4)  <br/>
Multidimensional analysis<br/>
![1](https://www.ruisitech.com/img/173641763.png?v5)  <br/>
Data report<br/>
![2](https://www.ruisitech.com/img/1693025478.png?v3)  <br/>
