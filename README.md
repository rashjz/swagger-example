# swagger-example
This product is only for testing and educational purpose 
###### Swagger useful links  
* http://localhost:9000/swagger-ui.html
* http://localhost:9000/v2/api-docs

###### Embedded Container https support added
* Create certificate for https 
```jshelllanguage
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
```

* redirect 8080 port to tomcat https (8443) port

```java
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "server.ssl", name = "enabled")
public class WebServerConfig {


    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(
                (TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));

        factory.addContextCustomizers(context -> {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            securityConstraint.addCollection(collection);
            context.addConstraint(securityConstraint);
        });

        factory.addAdditionalTomcatConnectors(initiateHttpConnector());

        return factory;
    }


    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
}

```
###### Docker Environment for development
* to create docker container run: 
```jshelllanguage
 docker-compose build
 docker-compose up
```
* For to connect docker container 
```jshelllanguage
 docker ps;
 docker docker exec -it <docker-id> /bin/bash
```
* For testing purpose to activate "pro" set VM parameter
```jshelllanguage
 -Dspring.profiles.active=pro
```
