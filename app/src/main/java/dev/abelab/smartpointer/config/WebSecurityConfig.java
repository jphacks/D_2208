package dev.abelab.smartpointer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import dev.abelab.smartpointer.auth.JWTAuthorizationFilter;
import dev.abelab.smartpointer.auth.LoginUserDetailsService;
import dev.abelab.smartpointer.auth.UserAuthenticationProvider;
import dev.abelab.smartpointer.property.AuthProperty;
import lombok.RequiredArgsConstructor;

/**
 * セキュリティの設定
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    final AuthenticationConfiguration authenticationConfiguration;

    private final LoginUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final AuthProperty authProperty;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("**.**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**",
            "/graphiql/**", "/graphql/**");
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        // CORSを有効化し、CSRF対策を無効化
        http.cors().and().csrf().disable();

        // アクセス許可
        http.authorizeRequests() //
            .antMatchers("/", "/api/health", "/ws/**", "/graphql/**").permitAll() //
            .antMatchers("/api/batch/**").hasIpAddress("::1") //
            .antMatchers("/**").permitAll() //
            .anyRequest().authenticated();

        // JWT認証
        http.addFilter(new JWTAuthorizationFilter(this.authenticationConfiguration.getAuthenticationManager(), this.authProperty,
            this.userDetailsService)) //
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    @Bean
    public UserAuthenticationProvider authenticationProvider() {
        final var authenticationProvider = new UserAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return authenticationProvider;
    }

}
