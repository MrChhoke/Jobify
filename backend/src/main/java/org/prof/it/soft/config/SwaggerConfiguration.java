package org.prof.it.soft.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Value("${server.port}")
    private String port;

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private List<Server> createServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + port)
                        .description("Local server")
        );
    }


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .servers(createServers())
                .components(new Components()
                        .addSecuritySchemes("Default authentication", createAPIKeyScheme()))
                .info(new Info().title("Jobify REST API")
                        .description("Spring Boot REST API for Jobify application")
                        .version("1.0").contact(new Contact().name("Vladyslav Bondar")
                                .email("www.linkedin.com/in/vladyslav-bondar03").url("vladbondar16133@gmail.com")));
    }
}
