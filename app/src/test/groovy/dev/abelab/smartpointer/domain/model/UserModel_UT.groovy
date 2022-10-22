package dev.abelab.smartpointer.domain.model

import dev.abelab.smartpointer.AbstractSpecification

/**
 * UserModelの単体テスト
 */
class UserModel_UT extends AbstractSpecification {

    def "builder: インスタンス生成時にIDが自動でセットされる"() {
        when:
        final user = UserModel.builder().build()

        then:
        user.id.length() == 36
    }

}
