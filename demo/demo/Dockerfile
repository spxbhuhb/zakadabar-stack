FROM openjdk:8-jdk-alpine
COPY local local
RUN apk update && apk add -y mc
ENTRYPOINT ["java","-jar","/local/demo/lib/demo-2021.3.26-all.jar", "--config", "/local/demo/etc/zakadabar-server-docker.yaml"]