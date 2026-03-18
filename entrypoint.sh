#!/usr/bin/env bash
# 默认 JVM 基础参数
DEFAULT_JVM_OPTS="-Xmx${HeapSize:-512m} -Xms${HeapSize:-512m} \
-XX:MetaspaceSize=${MetaspaceSize:-256m} -XX:MaxMetaspaceSize=${MetaspaceSize:-256m} \
-Dspring.profiles.active=${PROFILE:-dev} \
-Ddruid.mysql.usePingMethod=false \
-Duser.timezone=GMT+08 \
-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
-Dnacos_username=${NACOS_USERNAME:-0} \
-Dnacos_password=${NACOS_PASSWORD:-0} \
-Dnacos_server=${NACOS_SERVER:-0} \
-Dnacos_key=${NACOS_KEY:-0} \
-XX:+IgnoreUnrecognizedVMOptions -XX:+UseZGC \
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=.heapdump \
-XX:+PrintCommandLineFlags -XX:+DisableExplicitGC \
-Xlog:gc*,gc+heap=info,gc+age=trace:file=gc-%t.log:time,uptime,level,tags:filecount=10,filesize=10M"

# JDK 17 强一致性模块开放参数 (模块化解封)
MODULE_OPTS=""

# 执行 Java 进程 (使用 exec 确保 java 进程为 PID 1，以便接收退出信号)
exec java "${DEFAULT_JVM_OPTS}" "${MODULE_OPTS}" "${VM_OPTIONS}" -jar capable-admin.jar
