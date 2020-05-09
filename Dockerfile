FROM adoptopenjdk/openjdk11:jdk-11.0.7_10-centos 

RUN sed -i "/jdk.tls.disabledAlgorithms=/ s/=.*/=TLSv1.3, SSLv3, RC4, MD5withRSA, DH keySize < 1024, EC keySize < 224, DES40_CBC, RC4_40, 3DES_EDE_CBC/" $(readlink -f /usr/bin/java | sed "s:bin/java::")/conf/security/java.security

RUN mkdir /app

WORKDIR /app

COPY build/libs/code-checker.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "code-checker.jar" ]
