package dev.abelab.smartpointer.usecase

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * DisconnectPointerUseCaseの単体テスト
 */
class DisconnectPointerUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    DisconnectPointerUseCase sut

    def "handle: ポインターを切断する"() {
        given:
        final room = RandomHelper.mock(RoomModel)

        when:
        final result = this.sut.handle(room.id,)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        result.rotation == null
        !result.isActive
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