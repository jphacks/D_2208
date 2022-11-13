package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.domain.model.PointerControlModel
import dev.abelab.smartpointer.domain.model.RoomPointerModel
import dev.abelab.smartpointer.domain.model.UserModel
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
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
    Flux<RoomPointerModel> roomPointerFlux

    @Autowired
    Sinks.Many<RoomPointerModel> roomPointerSink

    @Autowired
    Flux<PointerControlModel> pointerControlFlux

    @Autowired
    Sinks.Many<PointerControlModel> pointerControlSink

    @Autowired
    Flux<UserModel> pointerDisconnectFlux

    @Autowired
    Sinks.Many<UserModel> pointerDisconnectSink

    def "ポインタータイプ取得API: 正常系 ポインタータイプを取得する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode | pointer_type
            "00000000-0000-0000-0000-000000000000" | ""       | "A"
        }
        // @formatter:on

        when:
        final query =
            """
                query {
                    getPointerType(roomId: "00000000-0000-0000-0000-000000000000")
                }
            """
        final response = this.executeWebSocket(query, "getPointerType", String)

        then:
        response == "A"
    }

    def "ポインタータイプ取得API: 異常系 ルームが存在しない場合は404エラー"() {
        expect:
        final query =
            """
                query {
                    getPointerType(roomId: "00000000-0000-0000-0000-000000000000")
                }
            """
        this.executeWebSocket(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "ポインター操作API: 正常系 ポインターを操作する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        final accessToken = this.getAccessToken(loginUser)

        when:
        final query =
            """
                mutation {
                    movePointer(alpha: 1.0, beta: 2.0, gamma: 3.0, accessToken: "${accessToken}") {
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


        expect:
        final query =
            """
                mutation {
                    movePointer(alpha: 1.0, beta: 2.0, gamma: 3.0, accessToken: "") {
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
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
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
        final accessToken = this.getAccessToken(loginUser)

        when:
        final query =
            """
                mutation {
                    disconnectPointer(accessToken: "${accessToken}") {
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

        expect:
        final query =
            """
                mutation {
                    disconnectPointer(accessToken: "") {
                        id
                        name
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "ポインタータイプ変更API: 正常系 ポインタータイプを変更する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode | pointer_type
            "00000000-0000-0000-0000-000000000000" | ""       | "A"
        }
        // @formatter:on

        when:
        final query =
            """
                mutation {
                    changePointerType(pointerType: "B", roomId: "00000000-0000-0000-0000-000000000000")
                }
            """
        final response = this.executeWebSocket(query, "changePointerType", String)

        then:
        response == "B"

        StepVerifier.create(this.roomPointerFlux)
            .expectNextMatches({
                it.roomId == "00000000-0000-0000-0000-000000000000" && it.pointerType == "B"
            })
            .thenCancel()
            .verify()
    }

    def "ポインタータイプ変更API: 異常系 ルームが存在しない場合は440エラー"() {

        expect:
        final query =
            """
                mutation {
                    changePointerType(pointerType: "", roomId: "00000000-0000-0000-0000-000000000000")
                }
            """
        this.executeWebSocket(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "ポインター操作購読API: 正常系 ポインターを購読できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")

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

    def "ポインター切断イベント購読API: 正常系 ポインター切断イベントを購読できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")

        final query = """
                subscription {
                    subscribeToPointerDisconnectEvent(roomId: "${loginUser.roomId}") {
                        id
                        name
                    }
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToPointerDisconnectEvent", User)

        when:
        final user1 = this.login("00000000-0000-0000-0000-000000000000")
        final user2 = this.login("00000000-0000-0000-0000-000000000000")
        final user3 = this.login("00000000-0000-0000-0000-000000000001")
        this.pointerDisconnectSink.tryEmitNext(user1)
        this.pointerDisconnectSink.tryEmitNext(user2)
        this.pointerDisconnectSink.tryEmitNext(user3)

        then:
        StepVerifier.create(response)
            .expectNextMatches({ it.id == user1.id && it.name == user1.name })
            .expectNextMatches({ it.id == user2.id && it.name == user2.name })
            .thenCancel()
            .verify()
    }

    def "ポインタータイプ購読API: 正常系 ポインタータイプを購読できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")

        final query = """
                subscription {
                    subscribeToPointerType(roomId: "${loginUser.roomId}")
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToPointerType", String)

        when:
        this.roomPointerSink.tryEmitNext(new RoomPointerModel("00000000-0000-0000-0000-000000000000", "A"))
        this.roomPointerSink.tryEmitNext(new RoomPointerModel("00000000-0000-0000-0000-000000000000", "B"))
        this.roomPointerSink.tryEmitNext(new RoomPointerModel("00000000-0000-0000-0000-000000000001", "C"))

        then:
        StepVerifier.create(response)
            .expectNextMatches({ it == "A" })
            .expectNextMatches({ it == "B" })
            .thenCancel()
            .verify()
    }

}
