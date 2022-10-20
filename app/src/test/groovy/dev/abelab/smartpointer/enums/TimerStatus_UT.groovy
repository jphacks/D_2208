package dev.abelab.smartpointer.enums

import dev.abelab.smartpointer.AbstractSpecification

/**
 * TimerStatusの単体テスト
 */
class TimerStatus_UT extends AbstractSpecification {

    def "find: IDからタイマーステータスを検索"() {
        when:
        final result = TimerStatus.find(inputId)

        then:
        result == expectedResult

        where:
        inputId                || expectedResult
        TimerStatus.READY.id   || Optional.of(TimerStatus.READY)
        TimerStatus.RUNNING.id || Optional.of(TimerStatus.RUNNING)
        -1                     || Optional.empty()
    }

}
