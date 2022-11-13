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
        room.id.length() == 11
        room.passcode.length() == 6
    }

    def "isPasscodeValid: パスコードが有効かチェック"() {
        given:
        final room = RoomModel.builder()
            .passcode("000000")
            .build()

        expect:
        room.isPasscodeValid(inputPasscode) == expectedResult

        where:
        inputPasscode || expectedResult
        "000000"      || true
        "000001"      || false
    }

}
