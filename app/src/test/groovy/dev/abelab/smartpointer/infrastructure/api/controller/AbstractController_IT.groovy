package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.AbstractDatabaseSpecification
import dev.abelab.smartpointer.domain.model.UserModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.property.AuthProperty
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.graphql.test.tester.WebSocketGraphQlTester
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

import java.time.Duration

/**
 * Controller統合テストの基底クラス
 */
abstract class AbstractController_IT extends AbstractDatabaseSpecification {

    private WebSocketGraphQlTester webSocketGraphQlTester

    @Autowired
    private HttpGraphQlTester httpGraphQlTester

    @Autowired
    private MessageSource messageSource

    @Autowired
    protected AuthProperty authProperty

    /**
     * Execute query with HTTP / return response
     *
     * @param query query
     * @param operation operation
     * @param clazz clazz
     * @return response
     */
    protected <T> T executeHttp(final String query, final String operation, final Class<T> clazz) {
        final response = this.httpGraphQlTester.document(query).execute()
            .path(operation)
            .entity(clazz)

        return response.get()
    }

    /**
     * Execute query with HTTP / verify exception
     *
     * @param query query
     * @param exception expected exception
     */
    protected executeHttp(final String query, final BaseException exception) {
        final expectedErrorMessage = this.getErrorMessage(exception)
        this.httpGraphQlTester.document(query).execute()
            .errors()
            .satisfy({
                assert it[0].errorType == exception.errorType
                assert it[0].message == expectedErrorMessage
            })
    }

    /**
     * Execute query with WebSocket
     *
     * @param query query
     */
    protected void executeWebSocket(final String query) {
        this.webSocketGraphQlTester.document(query).execute()
    }

    /**
     * Execute query with WebSocket / return response
     *
     * @param query query
     * @param operation operation
     * @param clazz clazz
     * @return response
     */
    protected <T> T executeWebSocket(final String query, final String operation, final Class<T> clazz) {
        final response = this.webSocketGraphQlTester.document(query).execute()
            .path(operation)
            .entity(clazz)

        return response.get()
    }

    /**
     * Execute query with WebSocket / verify exception
     *
     * @param query query
     * @param exception expected exception
     */
    protected executeWebSocket(final String query, final BaseException exception) {
        final expectedErrorMessage = this.getErrorMessage(exception)
        this.webSocketGraphQlTester.document(query).execute()
            .errors()
            .satisfy({
                assert it[0].errorType == exception.errorType
                assert it[0].message == expectedErrorMessage
            })
    }

    /**
     * Execute subscription query with WebSocket / return response
     *
     * @param query query
     * @param operation operation
     * @param clazz clazz
     * @return response
     */
    protected <T> Flux<T> executeWebSocketSubscription(final String query, final String operation, final Class<T> clazz) {
        return this.webSocketGraphQlTester.document(query).executeSubscription()
            .toFlux(operation, clazz)
    }

    /**
     * エラーメッセージを取得
     *
     * @param exception exception
     * @return エラーメッセージ
     */
    private String getErrorMessage(final BaseException exception) {
        final messageKey = exception.errorCode.messageKey
        final args = exception.args
        return this.messageSource.getMessage(messageKey, args, Locale.ENGLISH)
    }

    /**
     * ログイン
     *
     * @param roomId ルームID
     * @return ログインユーザ
     */
    protected UserModel login(final String roomId) {
        final user = UserModel.builder()
            .roomId(roomId)
            .name(RandomHelper.alphanumeric(10))
            .build()

        sql.dataSet("user").add(
            id: user.id,
            room_id: user.roomId,
            name: user.name,
        )

        return user
    }

    /**
     * GraphQL(over WebSocket)を開始
     *
     * @param user ユーザ
     */
    protected void connectWebSocketGraphQL(final UserModel user) {
        final var jwt = Jwts.builder()
            .setSubject(user.id)
            .setIssuer(this.authProperty.getJwt().getIssuer())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + this.authProperty.getTtl() * 1000))
            .signWith(SignatureAlgorithm.HS512, this.authProperty.getJwt().getSecret().getBytes())
            .compact()
        this.webSocketGraphQlTester = WebSocketGraphQlTester
            .builder("ws://localhost:${PORT}/graphql-ws", new ReactorNettyWebSocketClient())
            .header("Authorization", "Bearer " + jwt)
            .build()
    }


    /**
     * GraphQL(over WebSocket)を開始
     */
    protected void connectWebSocketGraphQL() {
        this.webSocketGraphQlTester = WebSocketGraphQlTester
            .builder("ws://localhost:${PORT}/graphql-ws", new ReactorNettyWebSocketClient())
            .build()
    }

    def setup() {
        StepVerifier.setDefaultTimeout(Duration.ofSeconds(5))
    }

}
