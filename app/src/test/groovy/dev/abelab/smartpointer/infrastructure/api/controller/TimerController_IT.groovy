package dev.abelab.smartpointer.infrastructure.api.controller


import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.exception.UnauthorizedException
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
        final response = this.executeHttp(query, "getTimer", Timer)

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
        this.executeHttp(query, new NotFoundException(expectedErrorCode))

        where:
        inputRoomId                           || expectedErrorCode
        "00000000-0000-0000-0000-000000000000" | ErrorCode.NOT_FOUND_TIMER
        "00000000-0000-0000-0000-000000000001" | ErrorCode.NOT_FOUND_ROOM
    }

    def "タイマー開始API: 正常系 タイマーを開始できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        this.connectWebSocketGraphQL(loginUser)

        when:
        final query =
            """
                mutation {
                    startTimer(inputTime: ${inputTime}) {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeWebSocket(query, "startTimer", Timer)

        then:
        final updatedTimer = sql.firstRow("SELECT * FROM timer")
        updatedTimer.input_time == inputTime
        updatedTimer.remaining_time_at_paused == null

        response.inputTime == inputTime
        response.remainingTimeAtPaused == null

        where:
        inputTime << [1, 3600]
    }

    def "タイマー開始API: 異常系 リクエストボディのバリデーション"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        this.connectWebSocketGraphQL(loginUser)

        expect:
        final query =
            """
                mutation {
                    startTimer(inputTime: ${inputTime}) {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new BadRequestException(expectedErrorCode))

        where:
        inputTime || expectedErrorCode
        0         || ErrorCode.INVALID_TIMER_INPUT_TIME
        3601      || ErrorCode.INVALID_TIMER_INPUT_TIME
    }

    def "タイマー開始API: 異常系 タイマーが準備中以外の場合は400エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status         | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | timerStatus.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        this.connectWebSocketGraphQL(loginUser)

        expect:
        final query =
            """
                mutation {
                    startTimer(inputTime: 100) {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new BadRequestException(expectedErrorCode))

        where:
        timerStatus         || expectedErrorCode
        TimerStatus.RUNNING || ErrorCode.TIMER_CANNOT_BE_STARTED
        TimerStatus.PAUSED  || ErrorCode.TIMER_CANNOT_BE_STARTED
    }

    def "タイマー開始API: 異常系 未認証の場合は401エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on

        this.connectWebSocketGraphQL()

        expect:
        final query =
            """
                mutation {
                    startTimer(inputTime: 100) {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN))
    }

}
