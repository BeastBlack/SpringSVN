package dev.blackbeast.springsvn.auth;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                    "/access-denied"));
            container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "/access-denied"));
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN,
                    "/access-denied"));
        };
    }
}
