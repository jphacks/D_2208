package dev.abelab.smartpointer.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import dev.abelab.smartpointer.property.AuthProperty;
import io.jsonwebtoken.Jwts;

/**
 * 認可フィルタ
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthProperty authProperty;

    private final UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(final AuthenticationManager authenticationManager, final AuthProperty authProperty,
        final UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.authProperty = authProperty;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
        final var authentication = this.getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        final var authorizationHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("");
        if (!authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        try {
            final var jwt = authorizationHeader.replace("Bearer ", "");
            final var subject = Jwts.parser() //
                .setSigningKey(this.authProperty.getJwt().getSecret().getBytes()) //
                .requireIssuer(this.authProperty.getJwt().getIssuer()) //
                .parseClaimsJws(jwt) //
                .getBody() //
                .getSubject();
            final var principal = this.userDetailsService.loadUserByUsername(subject);
            return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        } catch (final Exception e) {
            return null;
        }
    }

}
