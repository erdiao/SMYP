#!/bin/bash

# 设置Java运行参数
JAVA_OPTS="-Xms512m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m"

# 设置应用参数
APP_NAME="featherlink"
JAR_NAME="FeatherLink-0.0.1-SNAPSHOT.jar"
PID_FILE="application.pid"

# 如果PID文件存在，检查进程是否在运行
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p $PID > /dev/null; then
        echo "$APP_NAME is already running with pid $PID"
        exit 1
    else
        rm "$PID_FILE"
    fi
fi

# 启动应用
nohup java $JAVA_OPTS -jar $JAR_NAME --spring.profiles.active=prod > app.log 2>&1 &

# 保存PID
echo $! > "$PID_FILE"
echo "$APP_NAME is starting with pid $!" 