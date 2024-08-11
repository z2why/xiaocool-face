# 使用官方Java 8运行时作为基础镜像
FROM java:8

MAINTAINER yys(1009802251@qq.com)

# 设置时区为Asia/Shanghai
ENV TZ=Asia/Shanghai

#设置时区
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo '$TZ' > /etc/timezone

VOLUME /tmp
# 设置工作目录
WORKDIR /app

# 复制JAR文件和JKS文件到容器内
COPY uaa.jar /app/app.jar
COPY face.xiaoku.store.jks /app/face.xiaoku.store.jks

ENV SPRING-CLOUD-NACOS-ADDR ip:端口号
ENV SPRING-CLOUD-NACOS-IP ip.store
ENV SPRING-CLOUD-DOMAIN-NAME 域名:9091
ENV SPRING-CLOUD-MYSQL-HOST ip:端口号
ENV SPRING-CLOUD-GATEWAY-HOST ip:9091
ENV SPRING-CLOUD-DB-NAME meeting
ENV SPRING-CLOUD-REDIS-HOST ip
ENV SPRING-CLOUD-REDIS-PWD Yys12300
ENV NACOS-USERNAME nacos
ENV NACOS-PWD yys

EXPOSE 3080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
