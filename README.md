
<p align="center">
	<img alt="logo" height="100" width="300" src="/image/logo-black.png">
</p>

<h4 align="center">基于SpringBoot+React+Postgresql前后端分离的数据库实训平台</h4>
<p align="center">
	<a href="https://github.com/Open-DBT/open-dbt"><img src="https://img.shields.io/badge/opendbt-v2.0.0-brightgreen.svg"></a>
	<a href="https://github.com/Open-DBT/open-dbt/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>


## 项目说明
* 数据库实训平台是一个既能满足普通课程教学又能支撑起数据库系列课程教学的平台。
  主要用于各学校教师学生的教学和学习，也可用于公司或个人的培训、考试、练习。
  数据库实训平台不仅能够对普通客观题型（选择题、填空题、判断题等）进行自动评判，计算得分，而且能够对PostgreSQL数据库DML题型进行自动评判,计算得分。
  基于SpringBoot+React+PostgreSQL前后端分离的数据库实训平台,支撑起数据库系列课程及其他课程的教学。教师端在平台进行备课、教学、出题、发布作业、批改作业、查看学习进度等，学生端可在此平台进行上课学习、完成作业，使用该平台可提升课程质量和教学效率。整个教学过程可进行可持续的改进和优化，最终打造成一个一站式教学资源的整合平台。
* 前端采用React、antd-pro、ts
* 后端采用Spring Boot、Mybatis-Plus、Jwt
* 权限认证使用Jwt安全可控
* 支持Postgresql数据库
* 前端地址: https://github.com/Open-DBT/open-dbt-web
## 内置功能
* 用户管理: 完成平台用户的配置管理
* 角色管理: 角色权限分配
* 模块管理: 功能模块显示查询及各模块显示设置
* 个人设置: 设置个人信息，修改个人密码
* 意见反馈: 搜集用户意见
* 我的课程: 当前用户下的课程
* 课程模板: 课程专家和教师用户个人的课程模板，用于说明本学期开设的课程
* 班级: 当前教师角色下所有的班级信息，新建班级
* 介绍: 进入课程后的课程信息
* 章节: 用于教师备课、授课、编辑课程内容、统计学生学习进度，学生角色查看学生任务、个人学习进度统计、课程学习
* 知识: 该课程下知识点展示
* 题库: 用于教师端出题
* 作业: 用于教师端新建作业、发布作业、批改作业、查看学生作业情况
## 在线体验
* 演示地址：http://124.133.18.222:59005/user/login
* 账号密码: admin/admin
* 文档地址：https://github.com/Open-DBT/open-dbt/wiki
## 项目结构
```
─src
  │  └─main
  │      ├─java
  │      │  └─com
  │      │      └─highgo
  │      │          └─opendbt
  │      │              ├─api                 restful接口
  │      │              ├─catalogue           章节目录                  
  │      │              │  ├─domain
  │      │              │  │  └─entity
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─common               公共资源
  │      │              │  ├─advice
  │      │              │  ├─annotation
  │      │              │  ├─batchOperation
  │      │              │  ├─bean
  │      │              │  ├─constant
  │      │              │  ├─entity
  │      │              │  ├─events
  │      │              │  ├─exception
  │      │              │  │  ├─assertion
  │      │              │  │  ├─enums
  │      │              │  │  ├─handler
  │      │              │  │  └─i18n
  │      │              │  ├─Interval
  │      │              │  ├─listener
  │      │              │  ├─mapper
  │      │              │  ├─service
  │      │              │  │  └─impl
  │      │              │  └─utils
  │      │              ├─contents                章节内容
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─course                  课程相关
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─exercise                习题相关
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─feedback                 意见反馈相关
  │      │              │  ├─mapper
  │      │              │  ├─model
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─homework                 作业相关
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─manage
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─homeworkmodel            作业模板相关
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─login                     登录相关
  │      │              │  ├─mapper
  │      │              │  ├─model
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─migration                 历史数据迁移相关（忽略）
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─process                   学生学习进度相关
  │      │              │  ├─domain
  │      │              │  │  └─entity
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─progress                  学生练习相关（暂时不用）
  │      │              │  ├─mapper
  │      │              │  ├─model
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─resources                  资源相关
  │      │              │  ├─domain
  │      │              │  │  └─entity
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─sclass                      班级相关
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─statistics                  章节统计相关
  │      │              │  ├─domain
  │      │              │  │  └─model
  │      │              │  └─service
  │      │              │      └─impl
  │      │              ├─system                      系统设置相关
  │      │              │  ├─domain
  │      │              │  │  ├─entity
  │      │              │  │  └─model
  │      │              │  ├─mapper
  │      │              │  └─service
  │      │              │      └─impl
  │      ├─resources                                   配置信息
  │      └─webapp                                      静态资源信息
```
## 技术选型 
* 核心框架：Spring Boot 2.5.1
* 安全框架：Jwt 3.15.0
* 持久层框架：MyBatis-Plus 3.4.0
* 数据库连接池：HikariCP 4.0.3
* 日志管理：Logback
* 页面交互：React 17.0.0  
## 软件需求
* JDK1.8
* Maven3.0+
* PostgreSQL 9.4+
## 本地部署
### 开发环境搭建
* 1.git源码下载
`git clone https://github.com/Open-DBT/open-dbt.git`
* 2.lombok插件安装
<br />&emsp;idea File->setting->plugins->Marketplace 搜索 lombok ->install->Apply->OK->重启idea。
* 3.第三方包支持 
<br />&emsp;/lib目录下的aspose-cells-18.6.jar、aspose-words-18.6-jdk16.jar放到本地maven仓库com.aspose包下。
* 4.数据库安装配置
<br />&emsp;安装Postgresql数据库，新建数据库，编码模式UTF-8。
* 5.初始化数据库
<br />&emsp;在数据库publish模式下执行/src/main/resources/init.sql文件，初始化数据。
* 6.配置数据库信息
<br />&emsp;位置：/src/main/resources/application.properties
<br />&emsp;配置IP地址：`spring.datasource.url=jdbc:postgresql://XXX:X/test`
<br />&emsp;配置用户名：`spring.datasource.username=XXX`
<br />&emsp;配置密码：`spring.datasource.password=XXX`
### 部署
* 1.安装配置tomcat、jdk8并配置环境变量
* 2.使用maven打war包,名称为open-dbt.war
<br />&emsp;`mvn clean install`
<br />&emsp;`mvn clean pakage`
* 3.拷贝到webapps下
* 4.启动项目
<br />&emsp;1.切换至tomcat下bin目录
<br />&emsp;2.windows：双击startup.bat开启服务器
<br />&emsp;3.linux：执行命令&emsp;`./startup.sh`
### 访问
* 部署启动前端项目，请参考前端相关文档，前端项目地址：https://github.com/Open-DBT/open-dbt-web
* 登录地址: http://XXXX:XX/user/login
* 账号密码：admin/admin, js/js
## 如何贡献
* 提交一个问题或者功能, 请在提交前进行验证.
* 审阅 网站 ，对于任何拼写错误或者内容建议，请创建 pull requests
