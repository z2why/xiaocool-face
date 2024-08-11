docker build -t gateway .
docker run -p 9091:9091 -d --restart=always gateway
