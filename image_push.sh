#!/bin/sh

# 不包含docker login前奏， docker login手动登录后，密码会自动保存，所以也没必要在脚本中内置进来

mvn --settings D:/develop_tools/apache-maven-3.9.9/conf/settings-snowball.xml -U clean package

# 设置默认标签
tag=${1:-latest}
DOCKER_FILE=Dockerfile
NAMESPACE=capable-admin
# 基础仓库地址（不含标签）， 使用环境变量的方式，避免泄漏到脚本中，实际部署到服务器可以写死
BASE_URL=$BASE_REGISTRY_URL
REGISTRY_URL="$BASE_URL/$NAMESPACE:$tag"

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
