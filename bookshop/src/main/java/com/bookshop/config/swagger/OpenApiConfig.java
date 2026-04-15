package com.bookshop.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger3/OpenAPI 配置。
 * 作用：提供在线接口文档与 Bearer 鉴权调试能力。
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Bookshop API").version("v1").description("Bookshop 服务接口文档"))
                .components(new Components()
                        .addSecuritySchemes(
                                BEARER_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME));
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("auth").pathsToMatch("/api/auth/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("user").pathsToMatch("/api/users/**", "/api/audit/**").build();
    }

    @Bean
    public GroupedOpenApi bookApi() {
        return GroupedOpenApi.builder().group("book").pathsToMatch("/api/books/**").build();
    }
}
