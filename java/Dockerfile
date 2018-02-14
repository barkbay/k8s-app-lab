FROM maven:3.5.2-jdk-8
WORKDIR /usr/src/myapp
COPY pom.xml .
COPY src ./src
RUN mvn package

FROM openjdk:8
COPY --from=0 /usr/src/myapp/target/demo-0.0.1-SNAPSHOT.jar /app.jar
COPY entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]
