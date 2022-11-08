package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.helper.graphql.GraphQLOperation
import dev.abelab.smartpointer.helper.graphql.GraphQLQuery
import dev.abelab.smartpointer.infrastructure.api.response.AccessTokenResponse
import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse

/**
 * RoomControllerの統合テスト
 */
class RoomController_IT extends AbstractController_IT {

    def "ルーム作成API: 正常系 ルームを作成できる"() {
        when:
        final query = new GraphQLQuery<RoomResponse>(GraphQLOperation.CREATE_ROOM, [:], RoomResponse)
        final response = this.execute(query)

        then:
        final rooms = sql.rows("SELECT * FROM room")
        rooms.size() == 1

        response.roomId == rooms[0].id
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
        final query = new GraphQLQuery<String>(GraphQLOperation.DELETE_ROOM, [roomId: "00000000-0000-0000-0000-000000000000"], String)
        final response = this.execute(query)

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
        final query = new GraphQLQuery<String>(GraphQLOperation.DELETE_ROOM, [roomId: "00000000-0000-0000-0000-000000000001"], String)
        this.execute(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
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
        final query = new GraphQLQuery<AccessTokenResponse>(
            GraphQLOperation.JOIN_ROOM,
            [roomId: roomId, passcode: roomPasscode, userName: inputUserName],
            AccessTokenResponse
        )
        final response = this.execute(query)

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
        final query = new GraphQLQuery<AccessTokenResponse>(
            GraphQLOperation.JOIN_ROOM,
            [roomId: roomId, passcode: roomPasscode, userName: inputUserName],
            AccessTokenResponse
        )
        this.execute(query, new BadRequestException(expectedErrorCode))

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
        final query = new GraphQLQuery<AccessTokenResponse>(
            GraphQLOperation.JOIN_ROOM,
            [roomId: roomId + "...", passcode: roomPasscode, userName: RandomHelper.alphanumeric(10)],
            AccessTokenResponse
        )
        this.execute(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
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
        final query = new GraphQLQuery<AccessTokenResponse>(
            GraphQLOperation.JOIN_ROOM,
            [roomId: roomId, passcode: roomPasscode + "...", userName: RandomHelper.alphanumeric(10)],
            AccessTokenResponse
        )
        this.execute(query, new UnauthorizedException(ErrorCode.INCORRECT_ROOM_PASSCODE))
    }

    def "ルーム入室API: 異常系 ユーザ名が既に使われている場合は400エラー"() {
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

        expect:
        final query = new GraphQLQuery<AccessTokenResponse>(
            GraphQLOperation.JOIN_ROOM,
            [roomId: roomId, passcode: roomPasscode, userName: userName],
            AccessTokenResponse
        )
        this.execute(query, new BadRequestException(ErrorCode.USER_NAME_IS_ALREADY_EXISTS))
    }

}
