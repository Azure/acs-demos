FROM kodbasen/ubuntu-slim-armhf:16.04

ENV DEBIAN_FRONTEND noninteractive

ENV JAVA_VERSION=8 \
    JAVA_UPDATE=111 \
    JAVA_BUILD=14 \
    JAVA_HOME="/opt/jdk" \
    PATH=$PATH:${PATH}:/opt/jdk/bin \
    JAVA_OPTS="-server"

RUN apt-get -y update \
  && apt-get -y install curl \
  && curl -sSL --header "Cookie: oraclelicense=accept-securebackup-cookie;" "http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION}u${JAVA_UPDATE}-b${JAVA_BUILD}/jdk-${JAVA_VERSION}u${JAVA_UPDATE}-linux-arm32-vfp-hflt.tar.gz" | tar -xz \
  && echo "" > /etc/nsswitch.conf && \
  mkdir -p /opt && \
  mv jdk1.${JAVA_VERSION}.0_${JAVA_UPDATE} /opt/jdk-${JAVA_VERSION}u${JAVA_UPDATE}-b${JAVA_BUILD} && \
  ln -s /opt/jdk-${JAVA_VERSION}u${JAVA_UPDATE}-b${JAVA_BUILD} /opt/jdk && \
  ln -s /opt/jdk/jre/bin/java /usr/bin/java && \
  echo "hosts: files mdns4_minimal [NOTFOUND=return] dns mdns4" >> /etc/nsswitch.conf && \
  rm -rf $JAVA_HOME/jre/bin/jjs \
       $JAVA_HOME/jre/bin/keytool \
       $JAVA_HOME/jre/bin/orbd \
       $JAVA_HOME/jre/bin/pack200 \
       $JAVA_HOME/jre/bin/policytool \
       $JAVA_HOME/jre/bin/rmid \
       $JAVA_HOME/jre/bin/rmiregistry \
       $JAVA_HOME/jre/bin/servertool \
       $JAVA_HOME/jre/bin/tnameserv \
       $JAVA_HOME/jre/bin/unpack200 \
       $JAVA_HOME/man \
  rm /opt/jdk/src.zip && \
  apt-get -y autoremove && apt-get clean

WORKDIR /opt
RUN curl -O http://download.nextag.com/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
RUN tar -xzvf apache-maven-3.3.9-bin.tar.gz
ENV M2_HOME /opt/apache-maven-3.3.9
ENV PATH $PATH:$M2_HOME/bin

COPY ./maven-settings.xml /m2/maven-settings.xml

COPY ./core /usr/src/app/core
WORKDIR /usr/src/app/core
RUN mvn -s /m2/maven-settings.xml install

COPY ./trials-model /usr/src/app/trials-model
WORKDIR /usr/src/app/trials-model
RUN mvn -s /m2/maven-settings.xml install

