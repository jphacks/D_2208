package dev.abelab.smartpointer.enums

import dev.abelab.smartpointer.AbstractSpecification

/**
 * SlideControl単体テスト
 */
class SlideControl_UT extends AbstractSpecification {

    def "find: IDからタイマーステータスを検索"() {
        when:
        final result = SlideControl.find(inputId)

        then:
        result == expectedResult

        where:
        inputId                  || expectedResult
        SlideControl.NEXT.id     || Optional.of(SlideControl.NEXT)
        SlideControl.PREVIOUS.id || Optional.of(SlideControl.PREVIOUS)
        -1                       || Optional.empty()
    }

}
