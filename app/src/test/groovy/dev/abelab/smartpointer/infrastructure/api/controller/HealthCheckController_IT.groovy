package dev.abelab.smartpointer.infrastructure.api.controller


import dev.abelab.smartpointer.helper.graphql.GraphQLOperation
import dev.abelab.smartpointer.helper.graphql.GraphQLQuery

/**
 * HealthCheckControllerの統合テスト
 */
class HealthCheckController_IT extends AbstractController_IT {

    def "ヘルスチェックAPI: 正常系 trueを返す"() {
        when:
        final query = new GraphQLQuery<Boolean>(GraphQLOperation.HEALTH_CHECK, [:], Boolean)
        final response = this.execute(query)

        then:
        response
    }

}
