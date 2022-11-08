package dev.abelab.smartpointer.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.abelab.smartpointer.property.ProjectProperty;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;

/**
 * Swaggerの設定
 */
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ProjectProperty projectProperty;;

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder() //
            .group("Public API") //
            .packagesToScan("dev.abelab.smartpointer.infrastructure.api") //
            .build();
    }

    @Bean
    public OpenAPI openAPI() {
        final var info = new Info() //
            .title("Smart Pointer Internal API") //
            .version(this.projectProperty.getVersion());
        return new OpenAPI().info(info);
    }

}