package dev.abelab.smartpointer.infrastructure.api.controller

/**
 * HealthCheckControllerの統合テスト
 */
class HealthCheckController_IT extends AbstractController_IT {

    def "ヘルスチェックAPI: 正常系 trueを返す"() {
        when:
        final query =
            """
                query {
                    health
                }
            """
        final response = this.executeHttp(query, "health", Boolean)

        then:
        response
    }

}
