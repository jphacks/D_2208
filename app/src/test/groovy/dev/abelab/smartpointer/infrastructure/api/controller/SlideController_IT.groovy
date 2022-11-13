package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.domain.model.SlideControlModel
import dev.abelab.smartpointer.enums.SlideControl
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

/**
 * SlideControllerの統合テスト
 */
class SlideController_IT extends AbstractController_IT {

    @Autowired
    Flux<SlideControlModel> slideControlFlux

    @Autowired
    Sinks.Many<SlideControlModel> slideControlSink

    def "スライドを進めるAPI: 正常系 スライドを進める"() {
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
                    goNextSlide(accessToken: "${accessToken}")
                }
            """
        final response = this.executeWebSocket(query, "goNextSlide", SlideControl)

        then:
        response == SlideControl.NEXT

        StepVerifier.create(this.slideControlFlux)
            .expectNextMatches({ it.slideControl == SlideControl.NEXT })
            .thenCancel()
            .verify()
    }

    def "スライドを進めるAPI: 異常系 未認証の場合は401エラー"() {
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
                    goNextSlide(accessToken: "")
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "スライドを戻すAPI: 正常系 スライドを戻す"() {
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
                    goPreviousSlide(accessToken: "${accessToken}")
                }
            """
        final response = this.executeWebSocket(query, "goPreviousSlide", SlideControl)

        then:
        response == SlideControl.PREVIOUS

        StepVerifier.create(this.slideControlFlux)
            .expectNextMatches({ it.slideControl == SlideControl.PREVIOUS })
            .thenCancel()
            .verify()
    }

    def "スライドを戻すAPI: 異常系 未認証の場合は401エラー"() {
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
                    goPreviousSlide(accessToken: "")
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "スライド操作購読API: 正常系 スライド操作を購読できる"() {
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
                    subscribeToSlideControl(roomId: "${loginUser.roomId}")
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToSlideControl", SlideControl)

        when:
        this.slideControlSink.tryEmitNext(new SlideControlModel("00000000-0000-0000-0000-000000000000", SlideControl.NEXT))
        this.slideControlSink.tryEmitNext(new SlideControlModel("00000000-0000-0000-0000-000000000000", SlideControl.PREVIOUS))
        this.slideControlSink.tryEmitNext(new SlideControlModel("00000000-0000-0000-0000-000000000001", SlideControl.NEXT))

        then:
        StepVerifier.create(response)
            .expectNext(SlideControl.NEXT)
            .expectNext(SlideControl.PREVIOUS)
            .thenCancel()
            .verify()
    }

    def "スライド購読API: 正常系 別ルームのタイマー変更は通知されない"() {
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
                    subscribeToSlideControl(roomId: "${loginUser.roomId}")
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToSlideControl", SlideControl)

        when:
        this.slideControlSink.tryEmitNext(new SlideControlModel("00000000-0000-0000-0000-000000000001", SlideControl.NEXT))

        then:
        StepVerifier.create(response)
            .expectNextCount(0)
            .thenCancel()
            .verify()
    }

}
