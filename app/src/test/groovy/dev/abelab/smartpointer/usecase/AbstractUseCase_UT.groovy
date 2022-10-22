package dev.abelab.smartpointer.usecase

import dev.abelab.smartpointer.AbstractSpecification
import dev.abelab.smartpointer.domain.repository.RoomRepository
import dev.abelab.smartpointer.domain.repository.TimerRepository
import dev.abelab.smartpointer.domain.repository.UserRepository
import dev.abelab.smartpointer.domain.service.UserService
import org.spockframework.spring.SpringBean

/**
 * UseCase単体テストの基底クラス
 */
abstract class AbstractUseCase_UT extends AbstractSpecification {

    @SpringBean
    RoomRepository roomRepository = Mock()

    @SpringBean
    UserRepository userRepository = Mock()

    @SpringBean
    TimerRepository timerRepository = Mock()

    @SpringBean
    UserService userService = Mock()

}
