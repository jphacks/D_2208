package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse
import org.springframework.http.HttpStatus

/**
 * RoomRestControllerの統合テスト
 */
class RoomRestController_IT extends AbstractController_IT {

    static final String CREATE_ROOM_PATH = "/api/rooms"

    def "ルーム作成API: ルームを作成できる"() {
        when:
        final request = this.postRequest(CREATE_ROOM_PATH)
        final response = execute(request, HttpStatus.CREATED, RoomResponse)

        then:
        final rooms = sql.rows("SELECT * FROM room")
        rooms.size() == 1

        response.roomId == rooms[0].id
        response.token == rooms[0].token
    }

}
