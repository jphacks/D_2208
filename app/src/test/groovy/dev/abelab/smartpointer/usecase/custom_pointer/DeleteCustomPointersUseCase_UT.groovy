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
 * DeleteCustomPointersUseCaseの単体テスト
 */
class DeleteCustomPointersUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DeleteCustomPointersUseCase sut

    def "handle: カスタムポインターを削除"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointer = RandomHelper.mock(CustomPointerModel)

        when:
        this.sut.handle(customPointer.id, room.id)

        then:
        noExceptionThrown()
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.customPointerRepository.existsByIdAndRoomId(customPointer.id, room.id) >> true
        1 * this.customPointerRepository.deleteByIdAndRoomId(customPointer.id, room.id)
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointer = RandomHelper.mock(CustomPointerModel)

        when:
        this.sut.handle(customPointer.id, room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "handle: カスタムポインターが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointer = RandomHelper.mock(CustomPointerModel)

        when:
        this.sut.handle(customPointer.id, room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.customPointerRepository.existsByIdAndRoomId(customPointer.id, room.id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_CUSTOM_POINTER))
    }

}
