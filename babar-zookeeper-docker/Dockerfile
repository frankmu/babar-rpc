FROM openjdk:8-jre-alpine

ARG DISTRO_NAME=zookeeper-3.5.7

RUN apk add --no-cache wget bash \
    && mkdir /opt \
    && wget -q -O - "http://www.apache.org/dist/zookeeper/$DISTRO_NAME/apache-$DISTRO_NAME-bin.tar.gz" | tar -xzf - -C /opt \
    && mv /opt/apache-$DISTRO_NAME-bin /opt/zookeeper \
    && cp /opt/zookeeper/conf/zoo_sample.cfg /opt/zookeeper/conf/zoo.cfg

ENV ZOO_PORT 2181
EXPOSE $ZOO_PORT 2888 3888

WORKDIR /opt/zookeeper

ENTRYPOINT ["/opt/zookeeper/bin/zkServer.sh"]
CMD ["start-foreground"]
