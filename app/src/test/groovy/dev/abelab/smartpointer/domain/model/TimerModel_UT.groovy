package dev.abelab.smartpointer.domain.model

import dev.abelab.smartpointer.AbstractSpecification
import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.helper.DateHelper
import dev.abelab.smartpointer.helper.RandomHelper

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * TimerModelの単体テスト
 */
class TimerModel_UT extends AbstractSpecification {

    def "builder: インスタンス生成時にステータスが自動でセットされる"() {
        when:
        final timer = TimerModel.builder().build()

        then:
        timer.status == TimerStatus.READY
    }

    def "start: 準備中のタイマーを開始する"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.READY)
            .remainingTimeAtPaused(Optional.of(10))
            .build()

        when:
        timer.start(60)

        then:
        timer.inputTime == 60
        timer.remainingTimeAtPaused.isEmpty()
    }

    def "start: 準備中以外のタイマーは開始不可"() {
        given:
        final timer = TimerModel.builder()
            .status(status)
            .build()

        when:
        timer.start(60)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_STARTED))

        where:
        status << [
            TimerStatus.RUNNING,
            TimerStatus.PAUSED,
        ]
    }

    def "resume: 一時停止中のタイマーを再開する"() {
        given:
        final now = LocalDateTime.now()
        final timer = TimerModel.builder()
            .inputTime(120)
            .remainingTimeAtPaused(Optional.of(60))
            .status(TimerStatus.PAUSED)
            .build()

        when:
        timer.resume()

        then:
        ChronoUnit.MINUTES.between(now, timer.finishAt) == 1
        timer.remainingTimeAtPaused.isEmpty()
    }

    def "resume: 一時停止中以外のタイマーは再開不可"() {
        given:
        final timer = TimerModel.builder()
            .remainingTimeAtPaused(Optional.of(60))
            .status(TimerStatus.RUNNING)
            .build()

        when:
        timer.resume()

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESUMED))

        where:
        status << [
            TimerStatus.READY,
            TimerStatus.RUNNING,
        ]
    }

    def "pause: 実行中のタイマーを停止する"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.RUNNING)
            .remainingTimeAtPaused(Optional.empty())
            .finishAt(DateHelper.tomorrow())
            .build()

        when:
        timer.pause()

        then:
        timer.status == TimerStatus.PAUSED
        timer.remainingTimeAtPaused.isPresent()
    }

    def "pause: 実行中以外のタイマーは停止不可"() {
        given:
        final timer = TimerModel.builder()
            .status(status)
            .build()

        when:
        timer.pause()

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_PAUSED))

        where:
        status << [
            TimerStatus.READY,
            TimerStatus.PAUSED,
        ]
    }

    def "reset: タイマーをリセットする"() {
        given:
        final oldFinishAt = RandomHelper.mock(LocalDateTime)
        final timer = TimerModel.builder()
            .status(status)
            .inputTime(60)
            .remainingTimeAtPaused(Optional.of(10))
            .finishAt(oldFinishAt)
            .build()

        when:
        timer.reset()

        then:
        timer.remainingTimeAtPaused.isEmpty()
        timer.status == TimerStatus.READY
        timer.finishAt != oldFinishAt

        where:
        status << [
            TimerStatus.RUNNING,
            TimerStatus.PAUSED,
        ]
    }

    def "reset: 準備中のタイマーはリセット不可"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.READY)
            .build()

        when:
        timer.reset()

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESET))
    }

}
