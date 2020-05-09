FROM adoptopenjdk/openjdk11:jdk-11.0.7_10-centos 

RUN mkdir /app

WORKDIR /app

COPY build/libs/code-checker.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "code-checker.jar" ]
