package dev.abelab.smartpointer.domain.model

import dev.abelab.smartpointer.AbstractSpecification

/**
 * RoomModelの単体テスト
 */
class RoomModel_UT extends AbstractSpecification {

    def "builder: インスタンス生成時にIDとtokenが自動でセットされる"() {
        when:
        final room = RoomModel.builder().build()

        then:
        room.id.length() == 36
        room.token.length() == 44
    }

}
