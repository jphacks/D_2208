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

    def "existsByIdAndRoomId: ID、ルームIDからカスタムポインターの存在チェック"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
        }
        TableHelper.insert sql, "custom_pointer", {
            id                                     | room_id                                | label | url
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | ""    | ""
        }
        // @formatter:on

        when:
        final result = this.sut.existsByIdAndRoomId(inputId, inputRoomId)

        then:
        result == expectedResult

        where:
        inputId                                | inputRoomId                            || expectedResult
        "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" || true
        "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" || false
        "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000001" || false
    }

    def "deleteByIdAndRoomId: ID、ルームIDからカスタムポインターを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
        }
        TableHelper.insert sql, "custom_pointer", {
            id                                     | room_id                                | label | url
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | ""    | ""
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | ""    | ""
            "00000000-0000-0000-0000-000000000002" | "00000000-0000-0000-0000-000000000001" | ""    | ""
        }
        // @formatter:on

        when:
        this.sut.deleteByIdAndRoomId("00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000000")

        then:
        final customPointers = sql.rows("SELECT * FROM custom_pointer")
        customPointers*.id == ["00000000-0000-0000-0000-000000000001", "00000000-0000-0000-0000-000000000002"]
    }

}
