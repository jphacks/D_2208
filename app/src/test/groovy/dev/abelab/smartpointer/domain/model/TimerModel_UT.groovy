package dev.abelab.smartpointer.domain.model

import dev.abelab.smartpointer.AbstractSpecification
import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode

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
            .build()

        when:
        timer.start(60)

        then:
        timer.value == 60
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
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_IS_ALREADY_STARTED))
    }

    def "resume: タイマーを再開する"() {
        given:
        final timer = TimerModel.builder()
            .value(120)
            .status(TimerStatus.READY)
            .build()

        when:
        timer.resume(60)

        then:
        timer.value == 120
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
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_IS_ALREADY_STARTED))
    }

    def "stop: 実行中のタイマーを停止する"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.RUNNING)
            .build()

        when:
        timer.stop()

        then:
        timer.status == TimerStatus.READY
    }

    def "stop: 準備中のタイマーは停止不可"() {
        given:
        final timer = TimerModel.builder()
            .status(TimerStatus.READY)
            .build()

        when:
        timer.stop()

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.TIMER_IS_ALREADY_STOPPED))
    }

}
