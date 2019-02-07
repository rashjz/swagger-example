FROM java
ADD /swagger-example-0.0.1-SNAPSHOT.jar //
ENTRYPOINT ["java", "-jar", "/swagger-example-0.0.1-SNAPSHOT.jar"]
