package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.exception.*
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.request.LoginRequest
import org.springframework.http.HttpStatus

/**
 * AuthCheckRestControllerの統合テスト
 */
class AuthRestController_IT extends AbstractController_IT {

    // API PATH
    static final String BASE_PATH = "/api"
    static final String LOGIN_PATH = BASE_PATH + "/login"

    def "ログインAPI: 正常系 ログインに成功するとアクセストークンを返す"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomToken = RandomHelper.alphanumeric(10)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | token
            roomId | roomToken
        }
        // @formatter:on

        final requestBody = LoginRequest.builder()
            .roomId(roomId)
            .token(roomToken)
            .name(inputName)
            .build()

        when:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, HttpStatus.OK)

        then:
        final createdUser = sql.firstRow("SELECT * FROM user")
        createdUser.room_id == requestBody.roomId
        createdUser.name == requestBody.name

        where:
        inputName << [
            RandomHelper.alphanumeric(1),
            RandomHelper.alphanumeric(255),
        ]
    }

    def "ログインAPI: 異常系 リクエストボディのバリデーション"() {
        given:
        final requestBody = LoginRequest.builder()
            .roomId("")
            .token("")
            .name(inputName)
            .build()

        expect:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, new BadRequestException(expectedErrorCode))

        where:
        inputName                      || expectedErrorCode
        RandomHelper.alphanumeric(0)   || ErrorCode.INVALID_USER_NAME
        RandomHelper.alphanumeric(256) || ErrorCode.INVALID_USER_NAME
    }

    def "ログインAPI: 異常系 ルームが存在しない場合は404エラー"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomToken = RandomHelper.alphanumeric(10)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | token
            roomId | roomToken
        }
        // @formatter:on

        final requestBody = LoginRequest.builder()
            .roomId(roomId + "...")
            .token(roomToken)
            .name(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "ログインAPI: 異常系 トークンが間違えている場合は401エラー"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomToken = RandomHelper.alphanumeric(10)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | token
            roomId | roomToken
        }
        // @formatter:on

        final requestBody = LoginRequest.builder()
            .roomId(roomId)
            .token(roomToken + "...")
            .name(RandomHelper.alphanumeric(10))
            .build()

        expect:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, new UnauthorizedException(ErrorCode.INVALID_ROOM_TOKEN))
    }

    def "ログインAPI: 異常系 ユーザ名が既に使われている場合は409エラー"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomToken = RandomHelper.alphanumeric(10)
        final userName = RandomHelper.alphanumeric(10)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | token
            roomId | roomToken
        }
        TableHelper.insert sql, "user", {
            room_id | name
            roomId  | userName
        }
        // @formatter:on

        final requestBody = LoginRequest.builder()
            .roomId(roomId)
            .token(roomToken)
            .name(userName)
            .build()

        expect:
        final request = this.postRequest(LOGIN_PATH, requestBody)
        this.execute(request, new ConflictException(ErrorCode.USER_NAME_IS_ALREADY_EXISTS))
    }

}
