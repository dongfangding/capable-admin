#!/usr/bin/env bash

# 设置应用名称，用于日志命名
APP_NAME="capable-admin"
JAR_FILE="${APP_NAME}.jar"

# 定义变量时使用默认值
# 增加双引号防止变量为空时导致参数位移
HEAP_SIZE="${HeapSize:-512m}"
META_SIZE="${MetaspaceSize:-256m}"
ACTIVE_PROFILE="${PROFILE:-dev}"

# 确保日志目录存在
mkdir -p ./logs

# 将参数组织为数组（Array）是 Bash 中处理复杂参数最稳妥的方法
# 数组可以完美处理空格，且不需要担心字符串拼接时的引号嵌套问题
DEFAULT_JVM_OPTS=(
    "-Xmx${HEAP_SIZE}"
    "-Xms${HEAP_SIZE}"
    "-XX:+UseContainerSupport"
    "-XX:MetaspaceSize=${META_SIZE}"
    "-XX:MaxMetaspaceSize=${META_SIZE}"
    "-Dspring.profiles.active=${ACTIVE_PROFILE}"
    "-Ddruid.mysql.usePingMethod=false"
    "-Duser.timezone=GMT+08"
    "-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector"
    "-Dnacos_username=${NACOS_USERNAME:-0}"
    "-Dnacos_password=${NACOS_PASSWORD:-0}"
    "-Dnacos_server=${NACOS_SERVER:-0}"
    "-Dnacos_key=${NACOS_KEY:-0}"
    "-XX:+IgnoreUnrecognizedVMOptions"
    "-XX:+UseZGC"
    "-XX:+HeapDumpOnOutOfMemoryError"
    "-XX:+DisableExplicitGC"
    "-Xlog:gc*:file=./logs/gc-%t.log:time,uptime,level,tags:filecount=10,filesize=10M"
    "-XX:HeapDumpPath=./logs/${APP_NAME}-%t.hprof"
)

# JDK 17 模块开放参数
MODULE_OPTS=()

# 执行 Java 进程
# 使用 "${ARRAY[@]}" 语法可以确保数组中的每个元素被正确视为独立的参数
# 即使参数内部含有空格，也不会被拆分
exec java "${DEFAULT_JVM_OPTS[@]}" "${MODULE_OPTS[@]}" ${VM_OPTIONS:+"${VM_OPTIONS}"} -jar "${JAR_FILE}"
