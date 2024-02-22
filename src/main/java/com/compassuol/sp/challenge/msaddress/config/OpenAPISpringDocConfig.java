package com.compassuol.sp.challenge.msaddress.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPISpringDocConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
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
}
