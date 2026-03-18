#!/usr/bin/env bash

REGISTRY="registry.cn-hangzhou.aliyuncs.com"
SERVER_NAME=java-capable-admin
# 使用环境变量的方式，避免泄漏
: "${DOCKER_USER:?DOCKER_USER is required}"
: "${DOCKER_PWD:?DOCKER_PWD is required}"
: "${REGISTRY_NAMESPACE_URL:?REGISTRY_NAMESPACE_URL is required}"



# 登录，通过读取变量
printf '%s' "$DOCKER_PWD" | docker login \
  --username "$DOCKER_USER" \
  --password-stdin \
  "$REGISTRY"

mvn --settings D:/develop_tools/apache-maven-3.9.9/conf/settings-snowball.xml -U clean package
# 设置默认标签
tag=${1:-latest}
DOCKER_FILE=Dockerfile
# 基础仓库地址（不含标签）， 使用环境变量的方式，避免泄漏到脚本中，实际部署到服务器可以写死
REGISTRY_NAMESPACE_VAL="$REGISTRY_NAMESPACE_URL"
REGISTRY_URL="$REGISTRY_NAMESPACE_VAL/$SERVER_NAME:$tag"

echo "Starting build for: $REGISTRY_URL"

# 优先尝试使用 buildx 进行跨平台构建
if docker buildx build --platform linux/amd64 -t "$REGISTRY_URL" -f "$DOCKER_FILE" --push . ; then
    echo "Buildx upload successful."
else
    echo "Buildx failed or not supported, running fallback local build..."
    # 本地普通构建
    if docker build -f "$DOCKER_FILE" -t "$REGISTRY_URL" . ; then
        echo "Local build successful, pushing..."
        docker push "$REGISTRY_URL"
    else
        echo "Error: Both build methods failed."
        exit 1
    fi
fi

echo "All tasks completed."
