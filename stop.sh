#!/bin/bash

APP_NAME="featherlink"
PID_FILE="application.pid"

if [ ! -f "$PID_FILE" ]; then
    echo "PID file not found"
    exit 1
fi

PID=$(cat "$PID_FILE")

if ! ps -p $PID > /dev/null; then
    echo "Process $PID not found"
    rm "$PID_FILE"
    exit 1
fi

echo "Stopping $APP_NAME with pid $PID"
kill $PID

# 等待进程结束
for i in {1..30}; do
    if ! ps -p $PID > /dev/null; then
        echo "$APP_NAME stopped"
        rm "$PID_FILE"
        exit 0
    fi
    sleep 1
done

# 如果进程还在运行，强制结束
echo "Force stopping $APP_NAME"
kill -9 $PID
rm "$PID_FILE" 