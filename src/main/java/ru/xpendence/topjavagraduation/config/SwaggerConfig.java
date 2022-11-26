package ru.xpendence.topjavagraduation.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .in(SecurityScheme.In.HEADER)
                                                .scheme("bearer")
                                )
                )
                .security(List.of(new SecurityRequirement().addList("bearerAuth")))
                .info(new Info().title("Система голосования за ресторан"));
    }

    @Bean
    GroupedOpenApi adminApi() {
        return GroupedOpenApi
                .builder()
                .group("Админ")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    GroupedOpenApi userApi() {
        return GroupedOpenApi
                .builder()
                .group("Пользователь")
                .pathsToMatch("/user/**")
                .build();
    }
}
