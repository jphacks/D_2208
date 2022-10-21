package dev.abelab.smartpointer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * Web MVCの設定
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(final CorsRegistry registry) {
                registry //
                    .addMapping("/**") //
                    .allowedOrigins("*") //
                    .allowedMethods("*") //
                    .allowedHeaders("*") //
                    .exposedHeaders("*") //
                    .allowedOriginPatterns("*");
            }
        };
    }

}
