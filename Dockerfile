FROM eclipse-temurin:17-jre-jammy

# 安装 tzdata (时区)、fontconfig (字体配置) 和 fonts-dejavu (字体文件)
RUN apt-get update && apt-get install -y --no-install-recommends \
    tzdata \
    fontconfig \
    fonts-dejavu-core \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata \
    # 清理缓存，保持镜像精简
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /data
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh
COPY target/capable-admin.jar capable-admin.jar
COPY opentelemetry-javaagent.ja opentelemetry-javaagent.ja

# 默认环境变量
ENV PROFILE=local \
    HeapSize=512m \
    MetaspaceSize=256m \
    VM_OPTIONS=""

EXPOSE 8083

# 使用脚本启动
ENTRYPOINT ["./entrypoint.sh"]
