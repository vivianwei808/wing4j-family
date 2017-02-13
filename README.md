最新版本 [![Maven central](https://maven-badges.herokuapp.com/maven-central/org.wing4j/wing4j-family/badge.svg)](http://mvnrepository.com/search?q=org.wing4j)[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

![Wing4j](logo/wing4j_32.jpg "Wing4j 图标")[English Version](./README_EN.md)
![Wing4j](logo/wing4j_128.jpg "Wing4j 图标")
#wing4j家族#


| ![Wing4j Markdown](logo/wing4j_md_64.jpg "Wing4j Markdown 图标") | ![Wing4j ORM](logo/wing4j_orm_64.jpg "Wing4j ORM 图标") | ![Wing4j Test](logo/wing4j_test_64.jpg "Wing4j Test 图标") |                                          |                                          |                                          |                                          | ![Wing4j Config](logo/wing4j_config_64.jpg "Wing4j Config 图标") |      |      |
| ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---------------------------------------- | ---- | ---- |
| [wing4j Markdown](http://git.oschina.net/wing4j/wing4j-family/wikis/wing4j-markdown-%E6%96%87%E6%A1%A3%E6%A8%A1%E5%9E%8B) | [wing4j orm](http://git.oschina.net/wing4j/wing4j-family/wikis/%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8wing4j-orm) | [wing4j test](http://git.oschina.net/wing4j/wing4j-family/wikis/%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8wing4j-test) | [wing4j toolkit](http://git.oschina.net/wing4j/wing4j-family/wikis/%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8wing4j-toolkit) | [wing4j Common Logtrack](http://git.oschina.net/wing4j/wing4j-family/wikis/%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8wing4j-orm) | [wing4j Common Sequence](http://git.oschina.net/wing4j/wing4j-family/wikis/%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8wing4j-orm) | [wing4j Common Utils](http://git.oschina.net/wing4j/wing4j-family/wikis/%E4%BB%8E%E9%9B%B6%E5%BC%80%E5%A7%8B%E4%BD%BF%E7%94%A8wing4j-orm) | wing4j Config Center                     |      |      |
|                                          |                                          |                                          |                                          |                                          |                                          |                                          |                                          |      |      |

[在开源中国的项目介绍](https://www.oschina.net/p/wing4j-family)

##wing4j common##
提供开发过程中的公共程序，日志跟踪模块，序号服务生成服务。 

 1.wing4j common utils 

​	wing4j开发过程中用到的工具类

 2.wing4j common logtrack

​	wing4j 提供的日志跟踪实现，用于更好的记录日志，用于更好的日志查错。

 3.wing4j common sequence

​	wing4j提供的序号生成服务，连续自增序号的生成，支持单机，集群分布式环境

 4.wing4j common markdown

​	wing4j实现的markdown文档模型解析markdown文件，在orm中使用到该模块。

##wing4j orm##
目前基于MyBatis深度开发，完成单表的操作的自动生成，完全告别mybatis code generator方式，不再维护冗余的自动生成代码；
1. 基于注解方式申明表结构，支持JPA注解和Wing4j注解两种方式，JPA方式用于移植老旧的Hibernate程序；
2. 实现完整的物理分页功能，拥有简单易用；
3. 支持单元测试时开发数据源，可以进行单机免数据库方式和MySQL数据源方式，详细见wing4j test。

##wing4j test##
为TDD而生的测试开发支持，提供完整的数据库支持
1. 自动创建表结构，无论多少次运行单元测试，结果始终如一；
2. 开发数据可以进行本地数据库方式和MySQL数据源的选择；
3. 能够根据环境选择数据源，如果进行的是Maven编译，则不能使用MySQL数据源，必须使用本地数据源。

开始使用wing4j family
pom.xml添加如下代码
```xml
        <dependency>
            <groupId>org.wing4j.orm</groupId>
            <artifactId>wing4j-orm-mybatis</artifactId>
            <version>1.0.2-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.wing4j.test</groupId>
            <artifactId>wing4j-test-core</artifactId>
            <version>1.0.2-SNAPSHOT</version>
            <scope>test</scope>
        </dependency>
```

##向作者捐赠##

![微信捐赠](logo/donation.jpg "微信捐赠")

##加入QQ群获取技术支持## 

![QQ群](logo/QQ_group.jpg "QQ群")