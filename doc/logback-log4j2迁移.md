

步骤一 剔除旧的依赖，引入新的依赖 
主pom文件更改如下：



```xml
<log4j2.version>2.3</log4j2.version>
<disruptor.version>3.3.2</disruptor.version>
<!-- =================log4j2 relative configure=================== -->
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-api</artifactId>
   <version>${log4j2.version}</version>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-core</artifactId>
   <version>${log4j2.version}</version>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-slf4j-impl</artifactId>
   <version>${log4j2.version}</version>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-web</artifactId>
   <version>${log4j2.version}</version>
</dependency>
<!-- disruptor relative configure -->
<dependency>
   <groupId>com.lmax</groupId>
   <artifactId>disruptor</artifactId>
   <version>${disruptor.version}</version>
</dependency>
```



如果不是web项目，可以无需添加`log4j-web` 
另外，要使用异步写日志功能需添加disruptor，官方建议一般程序员查看的日志改成异步方式，一些运营日志改成同步，详情请见官方说明： 
[http://logging.apache.org/log4j/2.x/manual/async.html#UnderTheHood](http://logging.apache.org/log4j/2.x/manual/async.html#UnderTheHood) 
原有logback依赖不要物理删除，用exclusion 
` org.slf4j slf4j-api   ch.qos.logback logback-classic ` 
步骤二 删除logback.xml 新增log4j2.xml 
以IAPS为例

log4j2.xml配置如下



```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="WARN">
   <Properties>
      <Property name="LOG_HOME">/wls/applogs/rtlog </Property>
      <Property name="FILE_SIZE">300M</Property>
      <Property name="log_pattern">%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
      <Property name="rolling_file_name">-%d{yyyy-MM-dd}.%i.zip</Property>
      <Property name="rollover_strategy_max">30</Property>

      <Property name="LOG_HOME_BIZ">${LOG_HOME}/iapslogs/iaps-web/biz</Property>
      <Property name="LOG_HOME_ERROR">${LOG_HOME}/iapslogs/iaps-web/error</Property>
      <Property name="LOG_HOME_SERVICE">${LOG_HOME}/iapslogs/iaps-web/service</Property>
      <Property name="LOG_HOME_COMMON">${LOG_HOME}/iapslogs/iaps-web/common</Property>
        </Properties>
   <appenders>
      <Console name="Console" target="SYSTEM_OUT">
         <PatternLayout pattern="${log_pattern}" />
      </Console>
      <!-- biz log -->
      <!--<File name="bizRolling" fileName="${LOG_HOME_BIZ}/iaps-biz.txt" append="true">-->
         <!--<PatternLayout pattern="${log_pattern}"/>-->
      <!--</File>-->
      <RollingRandomAccessFile  name="bizRolling"
         fileName="${LOG_HOME_BIZ}/iaps-biz.txt"
         filePattern="${LOG_HOME_BIZ}/iaps-biz${rolling_file_name}"
         immediateFlush="false" append="true">
         <PatternLayout>
            <Pattern>${log_pattern}</Pattern>
            <Charset>UTF-8</Charset>
         </PatternLayout>
         <Policies>
            <SizeBasedTriggeringPolicy size="${FILE_SIZE}"/>
         </Policies>
         <DefaultRolloverStrategy max="${rollover_strategy_max}" />
      </RollingRandomAccessFile>

      <!-- service -->
      <RollingRandomAccessFile name="serviceRolling"
         fileName="${LOG_HOME_SERVICE}/iaps-service.txt"
         filePattern="${LOG_HOME_SERVICE}/iaps-service${rolling_file_name}"
         immediateFlush="false" append="true">
         <Filters>
            <ThresholdFilter level="ERROR" onMatch="DENY" onMismatch="ACCEPT" />
         </Filters>
         <PatternLayout>
            <Pattern>${log_pattern}</Pattern>
            <Charset>UTF-8</Charset>
         </PatternLayout>
         <Policies>
            <SizeBasedTriggeringPolicy size="${FILE_SIZE}" />
         </Policies>
         <DefaultRolloverStrategy max="${rollover_strategy_max}" />
      </RollingRandomAccessFile>

      <!-- error -->
      <RollingRandomAccessFile name="errorRolling"
         fileName="${LOG_HOME_ERROR}/iaps-error.txt"
         filePattern="${LOG_HOME_ERROR}/iaps-error${rolling_file_name}"
         immediateFlush="false" append="true">
         <Filters>
            <ThresholdFilter level="ERROR" onMatch="DENY" onMismatch="ACCEPT" />
         </Filters>
         <PatternLayout>
            <Pattern>${log_pattern}</Pattern>
            <Charset>UTF-8</Charset>
         </PatternLayout>
         <Policies>
            <SizeBasedTriggeringPolicy size="${FILE_SIZE}" />
         </Policies>
         <DefaultRolloverStrategy max="${rollover_strategy_max}" />
      </RollingRandomAccessFile>
      <!-- common -->
      <RollingRandomAccessFile name="iapsCommonRolling"
         fileName="${LOG_HOME_COMMON}/iaps-common.txt"
         filePattern="${LOG_HOME_COMMON}/iaps-common${rolling_file_name}"
         immediateFlush="false" append="true">
         <Filters>
            <ThresholdFilter level="ERROR" onMatch="DENY" onMismatch="ACCEPT" />
         </Filters>
         <PatternLayout>
            <Pattern>${log_pattern}</Pattern>
            <Charset>UTF-8</Charset>
         </PatternLayout>
         <Policies>
            <SizeBasedTriggeringPolicy size="${FILE_SIZE}" />
         </Policies>
         <DefaultRolloverStrategy max="${rollover_strategy_max}" />
      </RollingRandomAccessFile>
   </appenders>

   <loggers>
      <AsyncLogger  name="paff.common" level="error"/>
      <AsyncLogger name="paff.aop" level="error"/>
      <AsyncLogger name="paff.sms" level="error"/>
      <AsyncLogger name="paff.email" level="error"/>
      <AsyncLogger name="com.pinganfu.pingamq" level="error"/>

<AsyncRoot  level="info">
         <appender-ref ref="Console"/>
         <appender-ref ref="bizRolling" />
         <appender-ref ref="errorRolling" />
      </AsyncRoot>
   </loggers>
</configuration>
```



具体异步配置请参见官方文档 
[http://logging.apache.org/log4j/2.x/manual/async.html](http://logging.apache.org/log4j/2.x/manual/async.html) 
步骤三 在web.xml配置log4j2





```xml
<param-name>log4jConfiguration</param-name>
<param-value>log4j2.xml</param-value>
```



步骤四 修改LogUtil 
以IAPS为例 
Util的pom中添加



```xml
<dependency>
<groupId>org.apache.logging.log4j</groupId>
<artifactId>log4j-api</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-core</artifactId>
</dependency>
<dependency>
   <groupId>org.apache.logging.log4j</groupId>
   <artifactId>log4j-slf4j-impl</artifactId>
</dependency>
<dependency>
   <groupId>com.lmax</groupId>
   <artifactId>disruptor</artifactId>
</dependency>
```

然后修改LogUtil

至此，迁移基本完成。

