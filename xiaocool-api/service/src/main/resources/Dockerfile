# 使用官方Java 8运行时作为基础镜像
FROM openjdk:8-jre-slim
MAINTAINER yys(1009802251@qq.com)
VOLUME /tmp
# 设置工作目录
WORKDIR /app

# 复制JAR文件和JKS文件到容器内
COPY gateway.jar /app/app.jar
COPY xiaoku.store.jks /app/xiaoku.store.jks

ENV SPRING-CLOUD-NACOS-ADDR 服务器ip:应用端口号
ENV SPRING-CLOUD-MYSQL-HOST 服务器ip:应用端口号
ENV SPRING-CLOUD-DB-NAME meeting
ENV SPRING-CLOUD-REDIS-HOST 服务器ip

EXPOSE 8002
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
