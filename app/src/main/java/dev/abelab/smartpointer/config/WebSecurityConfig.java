package dev.abelab.smartpointer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

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
        return (web) -> web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORSを無効化
        http.cors().disable();

        // CSRF対策
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // アクセス許可
        http.authorizeRequests() //
            .antMatchers("/api/health").permitAll() //
            .antMatchers("/**").permitAll() //
            .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public <S extends Session> SessionRegistry sessionRegistry(final FindByIndexNameSessionRepository<S> sessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

}
