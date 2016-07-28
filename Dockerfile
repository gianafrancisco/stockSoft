FROM alpine:3.4

RUN mkdir -p /opt/target && mkdir -p /opt/src/main

COPY target/mpmStock-0.1.0.jar /opt/target/mpmStock-0.1.0.jar
COPY src/main/webapp /opt/src/main/webapp
COPY mysqlSchema.sh /opt/mysqlSchema.sh
COPY init.sh /opt/init.sh && chmod a+x /opt/init.sh

MAINTAINER Francisco Giana <gianafrancisco@gmail.com>

#RUN apt-get update && \
#	apt-get -y install default-jre && \
#	chmod a+x /opt/*.sh

#RUN echo "mysql-server mysql-server/root_password password toor" | debconf-set-selections && \
#	echo "mysql-server mysql-server/root_password_again password toor" | debconf-set-selections && \
#	apt-get -y install mysql-server && sh /opt/mysqlSchema.sh

EXPOSE 8080

ENTRYPOINT /opt/init.sh