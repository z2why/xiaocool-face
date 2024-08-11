# 使用官方Java 8运行时作为基础镜像
FROM openjdk:8-jre-slim

# 维护者信息
MAINTAINER yys(1009802251@qq.com)

# 设置工作目录
WORKDIR /app

# 复制JAR文件和JKS文件到容器内
COPY gateway.jar /app/app.jar
COPY xiaoku.store.jks /app/xiaoku.store.jks

# 设置环境变量
ENV SPRING-CLOUD-NACOS-ADDR 服务器ip:应用端口号，这里指的是nacos的端口号，具体看你服务开放在哪个端口
ENV SPRING-CLOUD-SENTINEL-DASHBOARD 服务器ip:应用端口号
ENV SPRING-CLOUD-MYSQL-HOST 服务器ip:应用端口号
ENV SPRING-CLOUD-DB-NAME meeting
ENV SPRING-CLOUD-REDIS-HOST 服务器ip

# 暴露端口
EXPOSE 9091

# 运行Spring Boot应用
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
