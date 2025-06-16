package com.giftservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for enhanced API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI giftServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gift Service API")
                        .description("REST API for managing gift suggestions and concrete gift implementations")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gift Service Team")
                                .email("support@giftservice.com")
                                .url("https://github.com/fwegener83/gift-service"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development server"),
                        new Server()
                                .url("https://api.giftservice.com")
                                .description("Production server")));
    }
}