package dev.abelab.smartpointer.infrastructure.api.controller

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
    Flux<SlideControl> slideControlFlux

    @Autowired
    Sinks.Many<SlideControl> slideControlSink

    def "スライドを進めるAPI: 正常系 スライドを進める"() {
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
                    goNextSlide
                }
            """
        final response = this.executeWebSocket(query, "goNextSlide", SlideControl)

        then:
        response == SlideControl.NEXT

        StepVerifier.create(this.slideControlFlux)
            .expectNext(SlideControl.NEXT)
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

        this.connectWebSocketGraphQL()

        expect:
        final query =
            """
                mutation {
                    goNextSlide
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
