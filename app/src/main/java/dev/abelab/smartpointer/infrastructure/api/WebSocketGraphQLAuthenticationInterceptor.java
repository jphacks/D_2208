package dev.abelab.smartpointer.infrastructure.api;

import java.util.Map;
import java.util.Objects;

import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * GraphQL (over WebSocket) Authentication Interceptor
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketGraphQLAuthenticationInterceptor implements WebSocketGraphQlInterceptor {

    private final UserRepository userRepository;

    @Override
    public Mono<WebGraphQlResponse> intercept(final WebGraphQlRequest request, final Chain chain) {
        log.info(request.getDocument());
        return WebSocketGraphQlInterceptor.super.intercept(request, chain);
    }

    @Override
    public Mono<Object> handleConnectionInitialization(final WebSocketSessionInfo sessionInfo,
        final Map<String, Object> connectionInitPayload) {
        final var principalName = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(String.format("GraphQL connection opened [user=%s]", principalName));

        return WebSocketGraphQlInterceptor.super.handleConnectionInitialization(sessionInfo, connectionInitPayload);
    }

    @Override
    public void handleConnectionClosed(final WebSocketSessionInfo sessionInfo, final int statusCode,
        final Map<String, Object> connectionInitPayload) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            log.info(String.format("%d: GraphQL connection closed [user=%s]", statusCode, authentication.getName()));
            this.userRepository.deleteById(authentication.getName());
        } else {
            log.info(String.format("%d: GraphQL connection closed", statusCode));
        }
        WebSocketGraphQlInterceptor.super.handleConnectionClosed(sessionInfo, statusCode, connectionInitPayload);
    }

}
