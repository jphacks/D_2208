package dev.abelab.smartpointer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * セキュリティの設定
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("**.**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORSを有効化し、CSRF対策を無効化
        http.cors().and().csrf().disable();

        // アクセス許可
        http.authorizeRequests() //
            .antMatchers("/", "/api/health", "/ws/**").permitAll() //
            .antMatchers("/api/batch/**").hasIpAddress("::1") //
            .antMatchers("/**").permitAll() //
            .anyRequest().authenticated();

        return http.build();
    }

}
