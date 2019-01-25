package rashjz.info.app.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@SpringBootApplication
@EnableConfigurationProperties
public class SwaggerExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwaggerExampleApplication.class, args);
	}
	@Bean
	public MultipartResolver filterMultipartResolver(){
		return new StandardServletMultipartResolver();
	}

	@Bean
	public ServletContextInitializer servletContextInitializer() {
		return servletContext -> servletContext.getSessionCookieConfig().setName("SW_APP_SESSION");
	}

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "|{}[]"));
		return factory;
	}

}

