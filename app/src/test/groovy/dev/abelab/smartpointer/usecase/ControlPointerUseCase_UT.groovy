package dev.abelab.smartpointer.usecase

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest
import org.springframework.beans.factory.annotation.Autowired

/**
 * ControlPointerUseCaseの単体テスト
 */
class ControlPointerUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    ControlPointerUseCase sut

    def "handle: ポインターを操作する"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final requestBody = RandomHelper.mock(PointerControlRequest)

        when:
        final result = this.sut.handle(room.id, requestBody)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        result.rotation.alpha == requestBody.alpha
        result.rotation.beta == requestBody.beta
        result.rotation.gamma == requestBody.gamma
        result.isActive
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = RandomHelper.mock(RoomModel)
        final requestBody = RandomHelper.mock(PointerControlRequest)

        when:
        this.sut.handle(room.id, requestBody)

        then:
        1 * this.roomRepository.existsById(room.id) >> false
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

}
