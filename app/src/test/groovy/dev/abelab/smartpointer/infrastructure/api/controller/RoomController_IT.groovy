package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse

/**
 * RoomControllerの統合テスト
 */
class RoomController_IT extends AbstractController_IT {

    def "ルーム作成API: 正常系 ルームを作成できる"() {
        when:
        final query =
            """
                mutation {
                    createRoom {
                        id
                        passcode
                    }
                }
            """
        final response = this.execute(query, "createRoom", RoomResponse)

        then:
        final rooms = sql.rows("SELECT * FROM room")
        rooms.size() == 1

        response.id == rooms[0].id
        response.passcode == rooms[0].passcode
    }

    def "ルーム削除API: 正常系 ルームを削除できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
            "00000000-0000-0000-0000-000000000001" | RandomHelper.numeric(6)
        }
        // @formatter:on

        when:
        final query =
            """
                mutation {
                    deleteRoom(roomId: "00000000-0000-0000-0000-000000000000")
                }
            """
        final response = this.execute(query, "deleteRoom", String)

        then:
        response == "00000000-0000-0000-0000-000000000000"

        final rooms = sql.rows("SELECT * FROM room")
        rooms*.id == ["00000000-0000-0000-0000-000000000001"]
    }

    def "ルーム削除API: 異常系 ルームが存在しない場合は404エラー"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | RandomHelper.numeric(6)
        }
        // @formatter:on

        expect:
        final query =
            """
                mutation {
                    deleteRoom(roomId: "00000000-0000-0000-0000-000000000001")
                }
            """
        this.execute(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

}
