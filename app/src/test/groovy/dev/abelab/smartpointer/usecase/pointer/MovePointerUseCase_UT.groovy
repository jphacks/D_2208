package dev.abelab.smartpointer.usecase.pointer


import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.domain.model.UserModel
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * MovePointerUseCaseの単体テスト
 */
class MovePointerUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    MovePointerUseCase sut

    def "handle: ポインターを操作する"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final loginUser = RandomHelper.mock(UserModel)

        when:
        final result = this.sut.handle(room.id, loginUser, 1.0, 2.0, 3.0)

        then:
        result.roomId == room.id
        result.user == loginUser
        result.orientation.alpha == 1.0
        result.orientation.beta == 2.0
        result.orientation.gamma == 3.0
    }

}
