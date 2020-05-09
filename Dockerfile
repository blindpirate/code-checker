FROM adoptopenjdk/openjdk11:jdk-11.0.7_10-centos 

RUN sed -i 's/jdk.tls.disabledAlgorithms=SSLv3/jdk.tls.disabledAlgorithms=SSLv3, TLSv1.3/g' /opt/java/openjdk/conf/security/java.security

RUN mkdir /app

WORKDIR /app

COPY build/libs/code-checker.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "code-checker.jar" ]
