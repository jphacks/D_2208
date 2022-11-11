package dev.abelab.smartpointer.usecase.custom_pointer

import dev.abelab.smartpointer.domain.model.CustomPointerModel
import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetCustomPointersUseCaseの単体テスト
 */
class GetCustomPointersUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetCustomPointersUseCase sut

    def "handle: カスタムポインターリストを取得"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointers = [
            RandomHelper.mock(CustomPointerModel),
            RandomHelper.mock(CustomPointerModel),
        ]

        when:
        final result = this.sut.handle(room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.customPointerRepository.selectByRoomId(room.id) >> customPointers
        result == customPointers
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
