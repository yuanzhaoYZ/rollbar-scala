# rollbar-scala
[![Travis master branch](https://img.shields.io/travis/storecove/rollbar-scala/master.svg)](https://travis-ci.org/storecove/rollbar-scala) [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/storecove/rollbar-scala?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![GitHub release](https://img.shields.io/github/release/storecove/rollbar-scala.svg)](https://github.com/storecove/rollbar-scala/releases) [![GitHub issues](https://img.shields.io/github/issues/storecove/rollbar-scala.svg)](https://github.com/storecove/rollbar-scala/issues) [![GitHub license](https://img.shields.io/github/license/storecove/rollbar-scala.svg)](https://github.com/storecove/rollbar-scala/blob/master/LICENSE)

This Notifier library(forked from [rollbar-scala][1]) works well with Spark 1.6+.
The rollbar-scala requires json4s version 3.2.11 while Spark still use a few years old version 3.2.10([JIRA][2]).
The fork is to shade json4s_3.2.11 and upgrade jackson-databind to 2.4.4 to work with spark.

[1]: https://github.com/storecove/rollbar-scala "Rollbar-scala"
[2]: https://issues.apache.org/jira/browse/SPARK-15123 "SPARK-15123"

## Build

```
sbt assembly
```

## Installation

For SBT:

```scala
libraryDependencies ++= Seq(
    "com.storecove" %% "rollbar-scala" % "1.1-SNAPSHOT",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
    "org.json4s" %% "json4s-jackson" % "3.2.11",
    "org.slf4j" % "slf4j-api" % "1.7.12"
)
```

In addition to these, you will need also an SLF4J logger implementation.

This library ships with concrete Rollbar appenders for both Log4j and Logback, so (if you haven't already done it) add one of them to your dependencies.

## Setup

You can implement your own appender or use the notifier as it is by getting a `RollbarNotifier` from a `RollbarNotifierFactory`.
Then you can use the `notify()` method to send a payload to the Rollbar service.

Or else you can use one of the default appenders (configuration examples follows).

### Log4j

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration debug="false" xmlns:log4j="http://jakarta.apache.org/log4j/"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://jakarta.apache.org/log4j/ ">

    <appender name="console" class="org.apache.log4j.ConsoleAppender">
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5p %C:%L - %m%n" />
        </layout>
    </appender>

    <appender name="ROLLBAR" class="com.storecove.rollbar.appenders.Log4jAppender">
        <param name="enabled" value="true"/>
        <param name="apiKey" value="${ROLLBAR_API_KEY}"/>
        <param name="environment" value="${ROLLBAR_ENV}"/>
        <param name="onlyThrowable" value="false" />
        <param name="notifyLevel" value="INFO" />
        <param name="limit" value="1000" />
        <param name="url" value="https://api.rollbar.com/api/1/item/" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yyyy-MM-dd HH:mm:ss} [%t] %-5p %C:%L - %m%n" />
        </layout>
    </appender>

    <root>
        <level value="${LOG_LEVEL}"/>
        <appender-ref ref="console"/>
        <appender-ref ref="ROLLBAR"/>
    </root>
</log4j:configuration>
```

### Logback

```xml
<configuration debug="true">
    <property file="${storecove_logging_file}" />

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}:%L - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="ROLLBAR" class="com.storecove.rollbar.appenders.LogbackAppender">
        <enabled>true</enabled>
        <apiKey>${ROLLBAR_API_KEY}</apiKey>
        <environment>${ROLLBAR_ENV}</environment>
        <onlyThrowable>false</onlyThrowable>
        <notifyLevel>INFO</notifyLevel>
    </appender>

    <root level="${LOG_LEVEL}">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="ROLLBAR"/>
    </root>
</configuration>
```
