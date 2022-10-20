package dev.abelab.smartpointer.infrastructure.api;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * STOMP Handshake Handler
 */
@Slf4j
public class StompHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(final ServerHttpRequest request, final WebSocketHandler wsHandler,
        final Map<String, Object> attributes) {
        // TODO: ルーム作成・入室APIでSecurityContextにログインユーザを入れれば多分動くはず！
        final var principal = request.getPrincipal();
        if (Objects.nonNull(principal)) {
            log.info(String.format("WebSocket connected: user=%s", principal.getName()));
        }

        return principal;
    }

}
