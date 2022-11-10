package dev.abelab.smartpointer.domain.model

import dev.abelab.smartpointer.AbstractSpecification
import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.helper.DateHelper
import dev.abelab.smartpointer.helper.RandomHelper

import java.time.LocalDateTime

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

    def "start: 実行中のタイマーは開始不可"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.RUNNING)
            .build()

        when:
        timer.start(60)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_STARTED))
    }

    def "resume: タイマーを再開する"() {
        given:
        final timer = TimerModel.builder()
            .inputTime(120)
            .status(TimerStatus.READY)
            .build()

        when:
        timer.resume(60)

        then:
        timer.inputTime == 120
    }

    def "resume: 実行中のタイマーは再開不可"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.RUNNING)
            .build()

        when:
        timer.resume(60)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_STARTED))
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
        timer.status == TimerStatus.READY
        timer.remainingTimeAtPaused.isPresent()
    }

    def "pause: 準備中のタイマーは停止不可"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.READY)
            .build()

        when:
        timer.pause()

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_PAUSED))
    }

    def "reset: 準備中のタイマーをリセットする"() {
        given:
        final oldFinishAt = RandomHelper.mock(LocalDateTime)
        final timer = TimerModel.builder()
            .status(TimerStatus.READY)
            .inputTime(60)
            .finishAt(oldFinishAt)
            .build()

        when:
        timer.reset()

        then:
        timer.status == TimerStatus.READY
        timer.finishAt != oldFinishAt
    }


    def "reset: 実行中のタイマーはリセット不可"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.RUNNING)
            .build()

        when:
        timer.reset()

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESET))
    }

}
