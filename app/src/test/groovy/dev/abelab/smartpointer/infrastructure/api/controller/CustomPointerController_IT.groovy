package dev.abelab.smartpointer.infrastructure.api.controller


import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.TableHelper
import dev.abelab.smartpointer.infrastructure.api.type.CustomPointers

/**
 * CustomPointerControllerの統合テスト
 */
class CustomPointerController_IT extends AbstractController_IT {

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

}
