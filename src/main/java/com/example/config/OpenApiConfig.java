package com.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "FeatherLink API",
        description = "FeatherLink 项目接口文档",
        version = "1.0"
    )
)
public class OpenApiConfig {
} 