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

    def "isTokenValid: トークンが有効かチェック"() {
        given:
        final room = RoomModel.builder()
            .token("A")
            .build()

        expect:
        room.isTokenValid(inputToken) == expectedResult

        where:
        inputToken || expectedResult
        "A"        || true
        "B"        || false
    }

}
