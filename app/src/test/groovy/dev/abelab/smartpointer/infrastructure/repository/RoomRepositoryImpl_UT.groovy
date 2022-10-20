package dev.abelab.smartpointer.infrastructure.repository

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * RoomRepositoryImplの単体テスト
 */
class RoomRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    RoomRepositoryImpl sut

    def "selectById: IDからルームを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | token
            "00000000-0000-0000-0000-000000000000" | "A"
        }
        // @formatter:on

        when:
        final result = this.sut.selectById("00000000-0000-0000-0000-000000000000")

        then:
        result.isPresent()
        result.get().id == "00000000-0000-0000-0000-000000000000"
        result.get().token == "A"
    }

    def "selectById: 存在しない場合はOptional.empty()を返す"() {
        when:
        final result = this.sut.selectById(RandomHelper.uuid())

        then:
        result.isEmpty()
    }

    def "insert: ルームを作成"() {
        given:
        final room = RoomModel.builder().build()

        when:
        this.sut.insert(room)

        then:
        final createdRoom = sql.firstRow("SELECT * FROM room")
        createdRoom.id == room.id
        createdRoom.token == room.token
    }

    def "deleteById: IDからルームを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | token
            "00000000-0000-0000-0000-000000000000" | ""
            "00000000-0000-0000-0000-000000000001" | ""
        }
        // @formatter:on

        when:
        this.sut.deleteById("00000000-0000-0000-0000-000000000000")

        then:
        final rooms = sql.rows("SELECT * FROM room")
        rooms*.id == ["00000000-0000-0000-0000-000000000001"]
    }

    def "existsById: IDからルームの存在チェック"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | token
            "00000000-0000-0000-0000-000000000000" | ""
        }
        // @formatter:on

        when:
        final result = this.sut.existsById(inputId)

        then:
        result == expectedResult

        where:
        inputId                                || expectedResult
        "00000000-0000-0000-0000-000000000000" || true
        "00000000-0000-0000-0000-000000000001" || false
    }

}
