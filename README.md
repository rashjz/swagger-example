# swagger-example

http://localhost:8080/swagger-ui.html#!/home-controller/getAppProductDetailsUsingGET

http://localhost:8080/v2/api-docs


* Create certificate for https 
###### keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650


* redirect 8080 port to tomcat https (8443) port

```java
    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
```

