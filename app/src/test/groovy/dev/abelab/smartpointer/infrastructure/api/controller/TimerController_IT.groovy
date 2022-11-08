package dev.abelab.smartpointer.infrastructure.api.controller


import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.type.Timer

/**
 * TimerControllerの統合テスト
 */
class TimerController_IT extends AbstractController_IT {

    def "タイマー取得API: 正常系 タイマーを取得できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status                | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.PAUSED.id | 60         | 30                       | "2000-01-01 10:30:30"
            "00000000-0000-0000-0000-000000000001" | TimerStatus.READY.id  | 100        | null                     | "2000-01-01 10:30:30"
        }
        // @formatter:on

        when:
        final query =
            """
                query {
                    getTimer(roomId: "${inputRoomId}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.execute(query, "getTimer", Timer)

        then:
        response.inputTime == expectedInputTime
        response.remainingTimeAtPaused == expectedRemainingTimeAtPaused
        response.status == expectedStatus
        response.finishAt.year == 2000
        response.finishAt.monthValue == 1
        response.finishAt.dayOfMonth == 1
        response.finishAt.hour == 10
        response.finishAt.minute == 30
        response.finishAt.second == 30

        where:
        inputRoomId                            || expectedStatus     | expectedInputTime | expectedRemainingTimeAtPaused
        "00000000-0000-0000-0000-000000000000" || TimerStatus.PAUSED | 60                | 30
        "00000000-0000-0000-0000-000000000001" || TimerStatus.READY  | 100               | null
    }

    def "タイマー取得API: 異常系 ルームもしくはタイマーが存在しない場合は404エラー"() {
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
                query {
                    getTimer(roomId: "${inputRoomId}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.execute(query, new NotFoundException(expectedErrorCode))

        where:
        inputRoomId                           || expectedErrorCode
        "00000000-0000-0000-0000-000000000000" | ErrorCode.NOT_FOUND_TIMER
        "00000000-0000-0000-0000-000000000001" | ErrorCode.NOT_FOUND_ROOM
    }

}
