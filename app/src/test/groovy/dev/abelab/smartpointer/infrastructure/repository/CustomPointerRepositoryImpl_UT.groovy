package dev.abelab.smartpointer.infrastructure.repository


import dev.abelab.smartpointer.domain.repository.CustomPointerRepository
import dev.abelab.smartpointer.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * CustomPointerRepositoryImplの単体テスト
 */
class CustomPointerRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    CustomPointerRepository sut

    def "selectByRoomId: ルームIDからカスタムポインターリストを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
        }
        TableHelper.insert sql, "custom_pointer", {
            id                                     | room_id                                | label | url
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"   | "A url"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"   | "B url"
            "00000000-0000-0000-0000-000000000002" | "00000000-0000-0000-0000-000000000001" | "C"   | "C url"
        }
        // @formatter:on

        when:
        final result = this.sut.selectByRoomId("00000000-0000-0000-0000-000000000000")

        then:
        result*.id == ["00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000001"]
        result*.roomId == ["00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000000"]
        result*.label == ["A", "B"]
        result*.url == ["A url", "B url"]
    }

}
