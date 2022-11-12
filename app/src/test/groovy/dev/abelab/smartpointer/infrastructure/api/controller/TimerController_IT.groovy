package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.domain.model.TimerModel
import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.DateHelper
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.type.Timer
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

import java.time.LocalDateTime

/**
 * TimerControllerの統合テスト
 */
class TimerController_IT extends AbstractController_IT {

    @Autowired
    Flux<TimerModel> timerFlux

    @Autowired
    Sinks.Many<TimerModel> timerSink

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
            "00000000-0000-0000-0000-000000000000" | TimerStatus.PAUSED.id | 60         | 30                       | "2099-01-01 10:30:30"
            "00000000-0000-0000-0000-000000000001" | TimerStatus.READY.id  | 100        | null                     | "2099-01-01 10:30:30"
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
        response.finishAt.year == 2099
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

    def "タイマー取得API: 正常系 RUNNINGだが終了時刻を過ぎている場合はREADYになる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status                | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.PAUSED.id | 60         | 30                       | DateHelper.yesterday()
        }
        // @formatter:on

        when:
        final query =
            """
                query {
                    getTimer(roomId: "00000000-0000-0000-0000-000000000000") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeHttp(query, "getTimer", Timer)

        then:
        response.status == TimerStatus.READY
        response.remainingTimeAtPaused == null
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

    def "タイマー開始API: 正常系 準備中のタイマーを開始できる"() {
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
        final accessToken = this.getAccessToken(loginUser)

        when:
        final query =
            """
                mutation {
                    startTimer(inputTime: ${inputTime},  accessToken: "${accessToken}") {
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
        updatedTimer.status == TimerStatus.RUNNING.id

        response.inputTime == inputTime
        response.remainingTimeAtPaused == null
        response.status == TimerStatus.RUNNING

        StepVerifier.create(this.timerFlux)
            .expectNextMatches({
                it.inputTime == inputTime && it.remainingTimeAtPaused.isEmpty() && it.status == TimerStatus.RUNNING
            })
            .thenCancel()
            .verify()

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
        final accessToken = this.getAccessToken(loginUser)

        expect:
        final query =
            """
                mutation {
                    startTimer(inputTime: ${inputTime}, accessToken: "${accessToken}") {
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
            "00000000-0000-0000-0000-000000000000" | timerStatus.id | 60         | 30                       | "2099-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        final accessToken = this.getAccessToken(loginUser)

        expect:
        final query =
            """
                mutation {
                    startTimer(inputTime: 100, accessToken: "${accessToken}") {
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

        expect:
        final query =
            """
                mutation {
                    startTimer(inputTime: 100, accessToken: "") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "タイマー再開API: 正常系 一時停止中のタイマーを再開できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status                | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.PAUSED.id | 60         | 30                       | "2099-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        final accessToken = this.getAccessToken(loginUser)

        when:
        final query =
            """
                mutation {
                    resumeTimer(accessToken: "${accessToken}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeWebSocket(query, "resumeTimer", Timer)

        then:
        final updatedTimer = sql.firstRow("SELECT * FROM timer")
        updatedTimer.input_time == 60
        updatedTimer.remaining_time_at_paused == null
        updatedTimer.status == TimerStatus.RUNNING.id

        response.inputTime == 60
        response.remainingTimeAtPaused == null
        response.status == TimerStatus.RUNNING

        StepVerifier.create(this.timerFlux)
            .expectNextMatches({
                it.inputTime == 60 && it.remainingTimeAtPaused.isEmpty() && it.status == TimerStatus.RUNNING
            })
            .thenCancel()
            .verify()
    }

    def "タイマー再開API: 異常系 タイマーが一時停止中以外の場合は400エラー"() {
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
        final accessToken = this.getAccessToken(loginUser)

        expect:
        final query =
            """
                mutation {
                   resumeTimer(accessToken: "${accessToken}") {
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
        TimerStatus.READY   || ErrorCode.TIMER_CANNOT_BE_RESUMED
        TimerStatus.RUNNING || ErrorCode.TIMER_CANNOT_BE_RESUMED
    }

    def "タイマー再開API: 異常系 未認証の場合は401エラー"() {
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


        expect:
        final query =
            """
                mutation {
                    resumeTimer(accessToken: "") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "タイマー一時停止API: 正常系 実行中のタイマーを一時停止できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status                 | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.RUNNING.id | 60         | 30                       | DateHelper.tomorrow().toString()
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        final accessToken = this.getAccessToken(loginUser)

        when:
        final query =
            """
                mutation {
                    pauseTimer(accessToken: "${accessToken}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeWebSocket(query, "pauseTimer", Timer)

        then:
        final updatedTimer = sql.firstRow("SELECT * FROM timer")
        updatedTimer.input_time == 60
        updatedTimer.remaining_time_at_paused != null
        updatedTimer.status == TimerStatus.PAUSED.id

        response.inputTime == 60
        response.remainingTimeAtPaused != null
        response.status == TimerStatus.PAUSED

        StepVerifier.create(this.timerFlux)
            .expectNextMatches({
                it.inputTime == 60 && it.remainingTimeAtPaused.isPresent() && it.status == TimerStatus.PAUSED
            })
            .thenCancel()
            .verify()
    }

    def "タイマー一時停止API: 異常系 タイマーが実行中以外の場合は400エラー"() {
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
        final accessToken = this.getAccessToken(loginUser)

        expect:
        final query =
            """
                mutation {
                    pauseTimer(accessToken: "${accessToken}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new BadRequestException(expectedErrorCode))

        where:
        timerStatus        || expectedErrorCode
        TimerStatus.READY  || ErrorCode.TIMER_CANNOT_BE_PAUSED
        TimerStatus.PAUSED || ErrorCode.TIMER_CANNOT_BE_PAUSED
    }

    def "タイマー一時停止API: 異常系 未認証の場合は401エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status                 | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.RUNNING.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on


        expect:
        final query =
            """
                mutation {
                    pauseTimer(accessToken: "") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "タイマーリセットAPI: 正常系 準備中以外のタイマーをリセットできる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status         | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | inputStatus.id | 60         | 30                       | "2099-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")
        final accessToken = this.getAccessToken(loginUser)

        when:
        final query =
            """
                mutation {
                    resetTimer(accessToken: "${accessToken}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeWebSocket(query, "resetTimer", Timer)

        then:
        final updatedTimer = sql.firstRow("SELECT * FROM timer")
        updatedTimer.input_time == 60
        updatedTimer.remaining_time_at_paused == null
        updatedTimer.status == TimerStatus.READY.id

        response.inputTime == 60
        response.remainingTimeAtPaused == null
        response.status == TimerStatus.READY

        StepVerifier.create(this.timerFlux)
            .expectNextMatches({
                it.inputTime == 60 && it.remainingTimeAtPaused.isEmpty() && it.status == TimerStatus.READY
            })
            .thenCancel()
            .verify()

        where:
        inputStatus << [
            TimerStatus.RUNNING,
            TimerStatus.PAUSED,
        ]
    }

    def "タイマーリセットAPI: 異常系 タイマー準備中の場合は400エラー"() {
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
        final accessToken = this.getAccessToken(loginUser)

        expect:
        final query =
            """
                mutation {
                    resetTimer(accessToken: "${accessToken}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESET))
    }

    def "タイマーリセットAPI: 異常系 未認証の場合は401エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status                 | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.RUNNING.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on


        expect:
        final query =
            """
                mutation {
                    resetTimer(accessToken: "") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        this.executeWebSocket(query, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))
    }

    def "タイマー購読API: 正常系 タイマー変更イベントを購読できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60         | 30                       | "2099-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")

        final query = """
                subscription {
                    subscribeToTimer(roomId: "${loginUser.roomId}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToTimer", Timer)

        when:
        final timer = TimerModel.builder()
            .roomId(loginUser.roomId)
            .inputTime(100)
            .remainingTimeAtPaused(Optional.empty())
            .finishAt(LocalDateTime.now())
            .status(TimerStatus.RUNNING)
            .build()
        this.timerSink.tryEmitNext(timer)

        then:
        StepVerifier.create(response)
            .expectNext(new Timer(timer))
            .thenCancel()
            .verify()
    }

    def "タイマー購読API: 正常系 別ルームのタイマー変更は通知されない"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | input_time | remaining_time_at_paused | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60         | 30                       | "2000-01-01 10:30:30"
            "00000000-0000-0000-0000-000000000001" | TimerStatus.READY.id | 60         | 30                       | "2000-01-01 10:30:30"
        }
        // @formatter:on

        final loginUser = this.login("00000000-0000-0000-0000-000000000000")

        final query = """
                subscription {
                    subscribeToTimer(roomId: "${loginUser.roomId}") {
                        inputTime
                        remainingTimeAtPaused
                        finishAt
                        status
                    }
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToTimer", Timer)

        when:
        final timer = TimerModel.builder()
            .roomId(loginUser.roomId + "...")
            .inputTime(100)
            .remainingTimeAtPaused(Optional.empty())
            .finishAt(LocalDateTime.now())
            .status(TimerStatus.RUNNING)
            .build()
        this.timerSink.tryEmitNext(timer)

        then:
        StepVerifier.create(response)
            .expectNextCount(0)
            .thenCancel()
            .verify()
    }

}
