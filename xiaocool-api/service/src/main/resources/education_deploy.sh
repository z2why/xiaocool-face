docker build -t service .
docker run -p 8002:8002 -d --restart=always service