package dev.abelab.smartpointer.domain.service

import dev.abelab.smartpointer.AbstractSpecification
import dev.abelab.smartpointer.domain.repository.UserRepository
import org.spockframework.spring.SpringBean

/**
 * Service単体テストの基底クラス
 */
abstract class AbstractService_UT extends AbstractSpecification {

    @SpringBean
    UserRepository userRepository = Mock()

}
