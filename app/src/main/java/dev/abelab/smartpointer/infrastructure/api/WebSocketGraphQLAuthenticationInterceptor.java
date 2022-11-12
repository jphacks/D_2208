package dev.abelab.smartpointer.infrastructure.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.RoomUsersEventModel;
import dev.abelab.smartpointer.domain.repository.UserRepository;
import dev.abelab.smartpointer.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * GraphQL (over WebSocket) Authentication Interceptor
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketGraphQLAuthenticationInterceptor implements WebSocketGraphQlInterceptor {

    private final Sinks.Many<RoomUsersEventModel> roomUsersEventSink;

    private final UserRepository userRepository;

    private final AuthUtil authUtil;

    @Override
    public Mono<WebGraphQlResponse> intercept(final WebGraphQlRequest request, final Chain chain) {
        if (request.getVariables().isEmpty()) {
            log.info(String.format("%s", request.getOperationName()));
        } else {
            log.info(String.format("%s %s", request.getOperationName(), request.getVariables()));
        }
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
        try {
            final var headers = (LinkedHashMap<String, String>) connectionInitPayload.get("headers");
            final var authorization = headers.get(HttpHeaders.AUTHORIZATION);
            final var loginUser = this.authUtil.getLoginUser(authorization.replace("Bearer ", ""));
            log.info(String.format("%d: GraphQL connection closed [name=%s, id=%s]", statusCode, loginUser.getName(), loginUser.getId()));
            this.userRepository.deleteById(loginUser.getId());
            this.roomUsersEventSink
                .tryEmitNext(new RoomUsersEventModel(loginUser.getRoomId(), this.userRepository.selectByRoomId(loginUser.getRoomId())));
        } catch (final Exception ignored) {
            log.info(String.format("%d: GraphQL connection closed", statusCode));
        }
        WebSocketGraphQlInterceptor.super.handleConnectionClosed(sessionInfo, statusCode, connectionInitPayload);
    }

}
