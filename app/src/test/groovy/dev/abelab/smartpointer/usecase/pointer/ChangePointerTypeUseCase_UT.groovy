package dev.abelab.smartpointer.usecase.pointer

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * ChangePointerTypeUseCaseの単体テスト
 */
class ChangePointerTypeUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    ChangePointerTypeUseCase sut

    def "handle: ポインターを切断する"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final pointerType = RandomHelper.alphanumeric(10)

        when:
        this.sut.handle(pointerType, room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.roomRepository.updatePointerTypeById(room.id, pointerType)
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final pointerType = RandomHelper.alphanumeric(10)

        when:
        this.sut.handle(pointerType, room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

}
