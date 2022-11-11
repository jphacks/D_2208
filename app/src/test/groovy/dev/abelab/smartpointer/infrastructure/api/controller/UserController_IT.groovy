package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.type.AccessToken
import dev.abelab.smartpointer.infrastructure.api.type.Users

/**
 * UserControllerの統合テスト
 */
class UserController_IT extends AbstractController_IT {

    def "ユーザリスト取得API: 正常系 ユーザリストを取得する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
            "00000000-0000-0000-0000-000000000002" | "000000"
        }
        TableHelper.insert sql, "user", {
            id                                     | room_id                                | name
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"
            "00000000-0000-0000-0000-000000000002" | "00000000-0000-0000-0000-000000000001" | "C"
        }
        // @formatter:on

        when:
        final query =
            """
                query {
                    getUsers(roomId: "${inputRoomId}") {
                        users {
                            id
                            name
                        }
                    }
                }
            """
        final response = this.executeHttp(query, "getUsers", Users)

        then:
        response.users*.name == expectedUserNames

        where:
        inputRoomId                            || expectedUserNames
        "00000000-0000-0000-0000-000000000000" || ["A", "B"]
        "00000000-0000-0000-0000-000000000001" || ["C"]
        "00000000-0000-0000-0000-000000000002" || []
    }

    def "ユーザリスト取得API: 異常系 ルームが存在しない場合は404エラー"() {
        expect:
        final query =
            """
                query {
                    getUsers(roomId: "") {
                        users {
                            id
                            name
                        }
                    }
                }
            """
        this.executeHttp(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
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

        when:
        final query =
            """
                mutation {
                    joinRoom(roomId: "${roomId}", passcode: "${roomPasscode}", userName: "${inputUserName}") {
                        tokenType
                        accessToken
                        ttl
                    }
                }
            """
        final response = this.executeHttp(query, "joinRoom", AccessToken)

        then:
        response.tokenType == this.authProperty.tokenType
        response.accessToken != null
        response.ttl == this.authProperty.ttl

        final createdUser = sql.firstRow("SELECT * FROM user")
        createdUser.room_id == roomId
        createdUser.name == inputUserName

        where:
        inputUserName << [
            RandomHelper.alphanumeric(1),
            RandomHelper.alphanumeric(255),
        ]
    }

    def "ルーム入室API: 異常系 リクエストボディのバリデーション"() {
        given:
        final roomId = RandomHelper.uuid()
        final roomPasscode = RandomHelper.numeric(6)

        // @formatter:off
        TableHelper.insert sql, "room", {
            id     | passcode
            roomId | roomPasscode
        }
        // @formatter:on

        expect:
        final query =
            """
                mutation {
                    joinRoom(roomId: "${roomId}", passcode: "${roomPasscode}", userName: "${inputUserName}") {
                        tokenType
                        accessToken
                        ttl
                    }
                }
            """
        this.executeHttp(query, new BadRequestException(expectedErrorCode))

        where:
        inputUserName                  || expectedErrorCode
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

        expect:
        final query =
            """
                mutation {
                    joinRoom(roomId: "${roomId + "..."}", passcode: "${roomPasscode}", userName: "...") {
                        tokenType
                        accessToken
                        ttl
                    }
                }
            """
        this.executeHttp(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
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

        expect:
        final query =
            """
                mutation {
                    joinRoom(roomId: "${roomId}", passcode: "${roomPasscode + "..."}", userName: "...") {
                        tokenType
                        accessToken
                        ttl
                    }
                }
            """
        this.executeHttp(query, new UnauthorizedException(ErrorCode.INCORRECT_ROOM_PASSCODE))
    }

}
