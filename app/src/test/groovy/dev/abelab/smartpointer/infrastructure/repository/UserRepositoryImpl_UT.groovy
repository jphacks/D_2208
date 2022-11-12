package dev.abelab.smartpointer.infrastructure.repository

import dev.abelab.smartpointer.domain.model.UserModel
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserRepositoryImplの単体テスト
 */
class UserRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    UserRepositoryImpl sut

    def "selectById: IDからユーザを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
        }
        TableHelper.insert sql, "user", {
            id                                     | room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"
        }
        // @formatter:on

        when:
        final result = this.sut.selectById("00000000-0000-0000-0000-000000000000")

        then:
        result.isPresent()
        result.get().id == "00000000-0000-0000-0000-000000000000"
        result.get().roomId == "00000000-0000-0000-0000-000000000000"
        result.get().name == "A"
    }

    def "selectById: 存在しない場合はOptional.empty()を返す"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
        }
        TableHelper.insert sql, "user", {
            id                                     | room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | ""
        }
        // @formatter:on

        when:
        final result = this.sut.selectById("00000000-0000-0000-0000-000000000001")

        then:
        result.isEmpty()
    }

    def "selectAll: ユーザリストを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
        }
        TableHelper.insert sql, "user", {
            id                                     | room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"
            "00000000-0000-0000-0000-000000000002" | "00000000-0000-0000-0000-000000000001" | "C"
        }
        // @formatter:on

        when:
        final result = this.sut.selectAll()

        then:
        result*.id == ["00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000001", "00000000-0000-0000-0000-000000000002"]
    }

    def "selectByRoomId: ルームIDからユーザリストを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
        }
        TableHelper.insert sql, "user", {
            id                                     | room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"
            "00000000-0000-0000-0000-000000000002" | "00000000-0000-0000-0000-000000000001" | "C"
        }
        // @formatter:on

        when:
        final roomId = "00000000-0000-0000-0000-000000000000"
        final result = this.sut.selectByRoomId(roomId)

        then:
        result*.id == ["00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000001"]
        result*.roomId == [roomId, roomId]
        result*.name == ["A", "B"]
    }

    def "insert: ユーザを作成"() {
        given:
        final roomId = RandomHelper.uuid()

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | "000000"
        }
        // @formatter:on

        final user = UserModel.builder()
            .roomId(roomId)
            .name(RandomHelper.alphanumeric(10))
            .build()

        when:
        this.sut.insert(user)

        then:
        final createdUser = sql.firstRow("SELECT * FROM user")
        createdUser.id == user.id
        createdUser.room_id == user.roomId
        createdUser.name == user.name
    }

    def "deleteById: IDからユーザを削除"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
        }
        TableHelper.insert sql, "user", {
            id                                     | room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"
        }
        // @formatter:on

        when:
        this.sut.deleteById("00000000-0000-0000-0000-000000000000")

        then:
        final users = sql.rows("SELECT * FROM user")
        users*.name == ["B"]
    }

    def "existsByRoomIdAndName: ルームID、ユーザ名からユーザの存在チェック"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
        }
        TableHelper.insert sql, "user", {
            room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "A"
        }
        // @formatter:on

        when:
        final result = this.sut.existsByRoomIdAndName(inputRoomId, inputName)

        then:
        result == expectedResult

        where:
        inputRoomId                            | inputName || expectedResult
        "00000000-0000-0000-0000-000000000000" | "A"       || true
        "00000000-0000-0000-0000-000000000001" | "A"       || false
        "00000000-0000-0000-0000-000000000000" | "B"       || false
    }

}
