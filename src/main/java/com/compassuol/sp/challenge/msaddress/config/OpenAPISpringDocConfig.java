package com.compassuol.sp.challenge.msaddress.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISpringDocConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("BearerTokenAuthentication")
                )
                .components(new Components()
                        .addSecuritySchemes("BearerTokenAuthentication", createSecurityScheme())
                )
                .info(new Info()
                        .title("CompassUOL - Challenge 3 - MS Address API")
                        .description("API em microsserviço para gerenciamento de endereços")
                        .version("v1")
                        .contact(new Contact()
                                .name("Bruno \"quasemago\" Ronning")
                                .email("brunoronningfn@gmail.com")
                                .url("https://github.com/quasemago"))
                );
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("BearerTokenAuthentication")
                .description("Insira um Bearer Token para autenticação")
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
