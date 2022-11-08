#!/bin/sh
nohup java -DKAFKA_BOOTSTRAP_SERVERS=localhost:9092 -DKAFKA_TOPIC=zipkin -jar zipkin-server-2.23.19-exec.jar --STORAGE_TYPE=mysql --MYSQL_HOST=localhost --MYSQL_TCP_PORT=3306 --MYSQL_DB=e-commerce-zipkin --MYSQL_USER=root --MYSQL_PASS=12345678 >> zipkin.log &

