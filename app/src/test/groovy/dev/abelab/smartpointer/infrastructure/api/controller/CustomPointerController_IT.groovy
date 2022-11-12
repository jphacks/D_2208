package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.domain.model.CustomPointerModel
import dev.abelab.smartpointer.domain.model.RoomCustomPointersModel
import dev.abelab.smartpointer.exception.ConflictException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
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

    def "カスタムポインター作成API: 正常系 カスタムポインターを作成する"() {
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

        when:
        final query =
            """
                mutation {
                    createCustomPointer(id: "00000000-0000-0000-0000-000000000001", roomId: "00000000-0000-0000-0000-000000000000", label: "A", content: "")
                }
            """
        final response = this.executeHttp(query, "createCustomPointer", String)

        then:
        response == "00000000-0000-0000-0000-000000000001"

        final createdCustomPointer = sql.firstRow("SELECT * FROM custom_pointer WHERE id=:id", [id: "00000000-0000-0000-0000-000000000001"])
        createdCustomPointer.id == "00000000-0000-0000-0000-000000000001"
        createdCustomPointer.room_id == "00000000-0000-0000-0000-000000000000"
        createdCustomPointer.label == "A"
        createdCustomPointer.url != null

        StepVerifier.create(this.roomCustomPointersFlux)
            .expectNextMatches({
                it.customPointers*.id == ["00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000001"]
            })
            .thenCancel()
            .verify()
    }

    def "カスタムポインター作成API: 異常系 カスタムポインターが既に存在する場合は400エラー"() {
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
                    createCustomPointer(id: "00000000-0000-0000-0000-000000000000", roomId: "00000000-0000-0000-0000-000000000000", label: "A", content: "")
                }
            """
        final response = this.executeHttp(query, new ConflictException(ErrorCode.CUSTOM_POINTER_IS_ALREADY_EXISTS))
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

    def "カスタムポインターリスト購読API: 正常系 ユーザリストを購読できる"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | "000000"
            "00000000-0000-0000-0000-000000000001" | "000000"
        }
        // @formatter:on

        final query =
            """
                subscription {
                    subscribeToCustomPointers(roomId: "00000000-0000-0000-0000-000000000000") {
                        customPointers {
                            id
                            label
                            url
                        }
                    }
                }
            """
        final response = this.executeWebSocketSubscription(query, "subscribeToCustomPointers", CustomPointers)

        when:
        this.roomCustomPointersSink.tryEmitNext(new RoomCustomPointersModel("00000000-0000-0000-0000-000000000000", [RandomHelper.mock(CustomPointerModel), RandomHelper.mock(CustomPointerModel)]))
        this.roomCustomPointersSink.tryEmitNext(new RoomCustomPointersModel("00000000-0000-0000-0000-000000000000", [RandomHelper.mock(CustomPointerModel)]))
        this.roomCustomPointersSink.tryEmitNext(new RoomCustomPointersModel("00000000-0000-0000-0000-000000000000", []))
        this.roomCustomPointersSink.tryEmitNext(new RoomCustomPointersModel("00000000-0000-0000-0000-000000000001", [RandomHelper.mock(CustomPointerModel)]))

        then:
        StepVerifier.create(response)
            .expectNextMatches({ it.customPointers.size() == 2 })
            .expectNextMatches({ it.customPointers.size() == 1 })
            .expectNextMatches({ it.customPointers.size() == 0 })
            .thenCancel()
            .verify()
    }

}
