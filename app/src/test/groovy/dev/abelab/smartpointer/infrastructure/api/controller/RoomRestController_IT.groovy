package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.exception.*
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.request.RoomJoinRequest
import dev.abelab.smartpointer.infrastructure.api.response.AccessTokenResponse
import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse
import org.springframework.http.HttpStatus

/**
 * RoomRestControllerの統合テスト
 */
class RoomRestController_IT extends AbstractController_IT {

    static final String BASE_PATH = "/api/rooms"
    static final String CREATE_ROOM_PATH = BASE_PATH
    static final String DELETE_ROOM_PATH = BASE_PATH + "/%s"
    static final String JOIN_ROOM_PATH = BASE_PATH + "/%s/join"

    def "ルーム作成API: 正常系 ルームを作成できる"() {
        when:
        final request = this.postRequest(CREATE_ROOM_PATH)
        final response = execute(request, HttpStatus.CREATED, RoomResponse)

        then:
        final rooms = sql.rows("SELECT * FROM room")
        rooms.size() == 1

        response.roomId == rooms[0].id
        response.passcode == rooms[0].passcode
    }

    def "ルーム削除API: 正常系 ルームを削除する"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomPasscode = RandomHelper.numeric(6)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | roomPasscode
        }
        // @formatter:on

        when:
        final request = this.deleteRequest(String.format(DELETE_ROOM_PATH, roomId))
        this.execute(request, HttpStatus.OK)

        then:
        final room = sql.firstRow("SELECT * FROM room WHERE id=:id", [id: roomId])
        room == null
    }

    def "ルーム削除API: 異常系 ルームが存在しない場合は404エラー"() {
        given:
        final roomId = RandomHelper.uuid()

        expect:
        final request = this.deleteRequest(String.format(DELETE_ROOM_PATH, roomId))
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "ルーム入室API: 正常系 入室に成功するとアクセストークンを返す"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomPasscode = RandomHelper.numeric(6)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | roomPasscode
        }
        // @formatter:on

        final requestBody = RoomJoinRequest.builder()
            .passcode(roomPasscode)
            .name(inputName)
            .build()

        when:
        final request = this.postRequest(String.format(JOIN_ROOM_PATH, roomId), requestBody)
        final response = this.execute(request, HttpStatus.OK, AccessTokenResponse)

        then:
        response.tokenType == this.authProperty.tokenType
        response.ttl == this.authProperty.ttl

        final createdUser = sql.firstRow("SELECT * FROM user")
        createdUser.room_id == roomId
        createdUser.name == requestBody.name

        where:
        inputName << [
            RandomHelper.alphanumeric(1),
            RandomHelper.alphanumeric(255),
        ]
    }

    def "ルーム入室API: 異常系 リクエストボディのバリデーション"() {
        given:
        final requestBody = RoomJoinRequest.builder()
            .passcode("")
            .name(inputName)
            .build()

        expect:
        final request = this.postRequest(String.format(JOIN_ROOM_PATH, "..."), requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputName                      || expectedErrorCode
        RandomHelper.alphanumeric(0)   || ErrorCode.INVALID_USER_NAME
        RandomHelper.alphanumeric(256) || ErrorCode.INVALID_USER_NAME
    }

    def "ルーム入室API: 異常系 ルームが存在しない場合は404エラー"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomPasscode = RandomHelper.numeric(6)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | roomPasscode
        }
        // @formatter:on

        final requestBody = RoomJoinRequest.builder()
            .passcode(roomPasscode)
            .name(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(String.format(JOIN_ROOM_PATH, "..."), requestBody)
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "ルーム入室API: 異常系 パスコードが間違えている場合は401エラー"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomPasscode = RandomHelper.numeric(6)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | roomPasscode
        }
        // @formatter:on

        final requestBody = RoomJoinRequest.builder()
            .passcode(roomPasscode + "...")
            .name(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(String.format(JOIN_ROOM_PATH, roomId), requestBody)
        this.execute(request, new UnauthorizedException(ErrorCode.INCORRECT_ROOM_PASSCODE))
    }

    def "ルーム入室API: 異常系 ユーザ名が既に使われている場合は409エラー"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomPasscode = RandomHelper.numeric(6)
        final userName = RandomHelper.alphanumeric(10)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | roomPasscode
        }
        TableHelper.insert sql, "user", {
            room_id | name
            roomId  | userName
        }
        // @formatter:on

        final requestBody = RoomJoinRequest.builder()
            .passcode(roomPasscode)
            .name(userName)
            .build()

        expect:
        final request = this.postRequest(String.format(JOIN_ROOM_PATH, roomId), requestBody)
        this.execute(request, new ConflictException(ErrorCode.USER_NAME_IS_ALREADY_EXISTS))
    }

}
