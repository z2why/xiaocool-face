docker build -t uaa .
docker run -p 3080:3080 -d  --restart=always uaa