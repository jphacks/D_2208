package dev.abelab.smartpointer.infrastructure.api.controller


import dev.abelab.smartpointer.domain.model.RoomCustomPointersModel
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.type.CustomPointers
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.test.StepVerifier

/**
 * CustomPointerControllerの統合テスト
 */
class CustomPointerController_IT extends AbstractController_IT {

    @Autowired
    Sinks.Many<RoomCustomPointersModel> roomCustomPointersSink

    @Autowired
    Flux<RoomCustomPointersModel> roomCustomPointersFlux

    def "カスタムポインターリスト取得API: 正常系 ユーザリストを取得する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
            "00000000-0000-0000-0000-000000000002" | "000000"
        }
        TableHelper.insert sql, "custom_pointer", {
            id                                     | room_id                                | label | url
            "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000000" | "A"   | "A url"
            "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" | "B"   | "B url"
            "00000000-0000-0000-0000-000000000002" | "00000000-0000-0000-0000-000000000001" | "C"   | "C url"
        }
        // @formatter:on

        when:
        final query =
            """
                query {
                    getCustomPointers(roomId: "${inputRoomId}") {
                        customPointers {
                            id
                            label
                            url
                        }
                    }
                }
            """
        final response = this.executeHttp(query, "getCustomPointers", CustomPointers)

        then:
        response.customPointers*.label == expectedLabels
        response.customPointers*.url == expectedUrls

        where:
        inputRoomId                            || expectedLabels | expectedUrls
        "00000000-0000-0000-0000-000000000000" || ["A", "B"]     | ["A url", "B url"]
        "00000000-0000-0000-0000-000000000001" || ["C"]          | ["C url",]
        "00000000-0000-0000-0000-000000000002" || []             | []
    }

    def "カスタムポインターリスト取得API: 異常系 ルームが存在しない場合は404エラー"() {
        expect:
        final query =
            """
                query {
                    getCustomPointers(roomId: "") {
                        customPointers {
                            id
                            label
                            url
                        }
                    }
                }
            """
        this.executeHttp(query, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "カスタムポインター削除API: 正常系 カスタムポインターを削除する"() {
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
        final query =
            """
                mutation {
                    deleteCustomPointer(id: "00000000-0000-0000-0000-000000000000", roomId: "00000000-0000-0000-0000-000000000000")
                }
            """
        final response = this.executeHttp(query, "deleteCustomPointer", String)

        then:
        response == "00000000-0000-0000-0000-000000000000"

        final customPointers = sql.rows("SELECT * FROM custom_pointer")
        customPointers*.id == ["00000000-0000-0000-0000-000000000001", "00000000-0000-0000-0000-000000000002"]

        StepVerifier.create(this.roomCustomPointersFlux)
            .expectNextMatches({
                it.customPointers*.id == ["00000000-0000-0000-0000-000000000001"]
            })
            .thenCancel()
            .verify()
    }

    def "カスタムポインター削除API: 異常系 ルームもしくはカスタムポインターが存在しない場合は404エラー"() {
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

        expect:
        final query =
            """
                mutation {
                    deleteCustomPointer(id: "${inputId}", roomId: "${inputRoomId}")
                }
            """
        this.executeHttp(query, new NotFoundException(expectedErrorCode))

        where:
        inputId                                | inputRoomId                            || expectedErrorCode
        "00000000-0000-0000-0000-000000000001" | "00000000-0000-0000-0000-000000000000" || ErrorCode.NOT_FOUND_CUSTOM_POINTER
        "00000000-0000-0000-0000-000000000000" | "00000000-0000-0000-0000-000000000001" || ErrorCode.NOT_FOUND_ROOM
    }

}
