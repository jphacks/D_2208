package dev.abelab.smartpointer.usecase.custom_pointer

import dev.abelab.smartpointer.domain.model.CustomPointerModel
import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ConflictException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateCustomPointersUseCaseの単体テスト
 */
class CreateCustomPointersUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    CreateCustomPointersUseCase sut

    def "handle: カスタムポインターを作成"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointer = RandomHelper.mock(CustomPointerModel)

        when:
        this.sut.handle(room.id, customPointer.id, customPointer.label, customPointer.url)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.customPointerRepository.existsByIdAndRoomId(customPointer.id, room.id) >> false
        1 * this.fileStorageUtil.upload(_)
        1 * this.customPointerRepository.insert(_)
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointer = RandomHelper.mock(CustomPointerModel)

        when:
        this.sut.handle(room.id, customPointer.id, customPointer.label, customPointer.url)

        then:
        1 * this.roomRepository.existsById(room.id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "handle: カスタムポインターが既に存在する場合は409エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final customPointer = RandomHelper.mock(CustomPointerModel)

        when:
        this.sut.handle(room.id, customPointer.id, customPointer.label, customPointer.url)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.customPointerRepository.existsByIdAndRoomId(customPointer.id, room.id) >> true
        final BaseException exception = thrown()
        verifyException(exception, new ConflictException(ErrorCode.CUSTOM_POINTER_IS_ALREADY_EXISTS))
    }

}
