FROM openjdk:8-jdk-alpine
COPY local local
RUN apk update && apk add -y mc
ENTRYPOINT ["java","-jar","/local/@projectName@/lib/@projectName@-@version@-all.jar", "--settings", "/local/@projectName@/etc/stack.server-docker.yaml"]
LABEL version="@version@"
EXPOSE 8080/tcp