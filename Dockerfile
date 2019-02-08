FROM java
ADD target/swagger-example-0.0.1-SNAPSHOT.jar //
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/swagger-example-0.0.1-SNAPSHOT.jar"]
