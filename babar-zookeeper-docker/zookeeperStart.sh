docker build -t frankmu/zookeeper:3.5.7 .
docker run -p 2181:2181 -p 2888:2888 -p 3888:3888 frankmu/zookeeper:3.5.7
