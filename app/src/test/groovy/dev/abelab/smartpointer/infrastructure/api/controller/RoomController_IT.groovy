package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.helper.graphql.GraphQLOperation
import dev.abelab.smartpointer.helper.graphql.GraphQLQuery
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
        // TODO: テストを書く
    }

    def "ルーム入室API: 異常系 リクエストボディのバリデーション"() {
        // TODO: テストを書く
    }

    def "ルーム入室API: 異常系 ルームが存在しない場合は404エラー"() {
        // TODO: テストを書く
    }

    def "ルーム入室API: 異常系 パスコードが間違えている場合は401エラー"() {
        // TODO: テストを書く
    }

    def "ルーム入室API: 異常系 ユーザ名が既に使われている場合は409エラー"() {
        // TODO: テストを書く
    }

}
