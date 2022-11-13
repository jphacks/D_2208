package dev.abelab.smartpointer.usecase.pointer

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GetPointerTypeUseCaseの単体テスト
 */
class GetPointerTypeUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GetPointerTypeUseCase sut

    def "handle: ポインタータイプを取得する"() {
        given:
        final room = RandomHelper.mock(RoomModel)

        when:
        final result = this.sut.handle(room.id)

        then:
        1 * this.roomRepository.selectById(room.id) >> Optional.of(room)
        result == room.pointerType
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)

        when:
        this.sut.handle(room.id)

        then:
        1 * this.roomRepository.selectById(room.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

}
