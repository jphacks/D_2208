package dev.abelab.smartpointer.usecase.user

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.domain.model.UserModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetUsersUseCaseの単体テスト
 */
class GetUsersUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetUsersUseCase sut

    def "handle: ユーザリストを取得"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final users = [
            RandomHelper.mock(UserModel),
            RandomHelper.mock(UserModel),
        ]

        when:
        final result = this.sut.handle(room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.userRepository.selectByRoomId(room.id) >> users
        result == users
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)

        when:
        this.sut.handle(room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

}
