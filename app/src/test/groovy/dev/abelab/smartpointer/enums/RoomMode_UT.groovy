package dev.abelab.smartpointer.enums

import dev.abelab.smartpointer.AbstractSpecification

/**
 * RoomModeの単体テスト
 */
class RoomMode_UT extends AbstractSpecification {

    def "find: IDからルームモードを検索"() {
        when:
        final result = RoomMode.find(id)

        then:
        result == expectedResult

        where:
        id                       || expectedResult
        RoomMode.PRESENTATION.id || Optional.of(RoomMode.PRESENTATION)
        RoomMode.WEB_MEETING.id  || Optional.of(RoomMode.WEB_MEETING)
        -1                       || Optional.empty()
    }

}
