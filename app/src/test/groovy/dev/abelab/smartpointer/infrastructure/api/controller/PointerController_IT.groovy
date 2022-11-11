package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.domain.model.PointerControlModel
import dev.abelab.smartpointer.domain.model.UserModel
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.type.PointerControl
import dev.abelab.smartpointer.infrastructure.api.type.User
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

/**
 * PointerControllerの統合テスト
 */
class PointerController_IT extends AbstractController_IT {

    @Autowired
    Flux<PointerControlModel> pointerControlFlux

    @Autowired
    Sinks.Many<PointerControlModel> pointerControlSink

    @Autowired
    Flux<UserModel> pointerDisconnectFlux

    @Autowired
    Sinks.Many<UserModel> pointerDisconnectSink

    def "ポインター操作API: 正常系 ポインターを操作する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        this.connectWebSocketGraphQL(loginUser)

        when:
        final query =
            """
                mutation {
                    movePointer(alpha: 1.0, beta: 2.0, gamma: 3.0) {
                        user {
                            id
                            name
                        }
                        orientation {
                            alpha
                            beta
                            gamma
                        }
                    }
                }
            """
        final response = this.executeWebSocket(query, "movePointer", PointerControl)

        then:
        response.user.id == loginUser.id
        response.user.name == loginUser.name
        response.orientation.alpha == 1.0
        response.orientation.beta == 2.0
        response.orientation.gamma == 3.0

        StepVerifier.create(this.pointerControlFlux)
            .expectNextMatches({
                it.user.id == loginUser.id
                    && it.user.name == loginUser.name
                    && it.user.roomId == loginUser.roomId
                    && it.orientation.alpha == 1.0
                    && it.orientation.beta == 2.0
                    && it.orientation.gamma == 3.0
            })
            .thenCancel()
            .verify()
    }

    def "ポインター操作API: 異常系 未認証の場合は401エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        // @formatter:on

        this.connectWebSocketGraphQL()

        expect:
        final query =
            """
                mutation {
                    movePointer(alpha: 1.0, beta: 2.0, gamma: 3.0) {
                        user {
                            id
                            name
                        }
                        orientation {
                            alpha
                            beta
                            gamma
                        }
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ポインター切断API: 正常系 ポインターを切断する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        this.connectWebSocketGraphQL(loginUser)

        when:
        final query =
            """
                mutation {
                    disconnectPointer {
                        id
                        name
                    }
                }
            """
        final response = this.executeWebSocket(query, "disconnectPointer", User)

        then:
        response.id == loginUser.id
        response.name == loginUser.name

        StepVerifier.create(this.pointerDisconnectFlux)
            .expectNextMatches({
                it.id == loginUser.id && it.name == loginUser.name && it.roomId == loginUser.roomId
            })
            .thenCancel()
            .verify()
    }

    def "ポインター切断API: 異常系 未認証の場合は401エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        // @formatter:on

        this.connectWebSocketGraphQL()

        expect:
        final query =
            """
                mutation {
                    disconnectPointer {
                        id
                        name
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

    def "ポインター操作購読API: ポインターを購読できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        this.connectWebSocketGraphQL(loginUser)

        final query = """
                subscription {
                    subscribeToPointer(roomId: "${loginUser.roomId}") {
                        user {
                            id
                            name
                        }
                        orientation {
                            alpha
                            beta
                            gamma
                        }
                    }
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToPointer", PointerControl)

        when:
        this.pointerControlSink.tryEmitNext(new PointerControlModel("00000000-0000-0000-0000-000000000000", loginUser, 1.0, 2.0, 3.0))
        this.pointerControlSink.tryEmitNext(new PointerControlModel("00000000-0000-0000-0000-000000000000", loginUser, 0.0, 0.0, 0.0))
        this.pointerControlSink.tryEmitNext(new PointerControlModel("00000000-0000-0000-0000-000000000001", loginUser, 0.0, 0.0, 0.0))

        then:
        StepVerifier.create(response)
            .expectNextMatches({
                it.user.id == loginUser.id
                    && it.user.name == loginUser.name
                    && it.orientation.alpha == 1.0
                    && it.orientation.beta == 2.0
                    && it.orientation.gamma == 3.0
            })
            .expectNextMatches({
                it.user.id == loginUser.id
                    && it.user.name == loginUser.name
                    && it.orientation.alpha == 0.0
                    && it.orientation.beta == 0.0
                    && it.orientation.gamma == 0.0
            })
            .thenCancel()
            .verify()
    }

}
