package com.example.springbatchdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Spring Batch CSV processor")
                        .description("Rest service for spring batch csv processor")
                        .version("v1"));
    }
}
