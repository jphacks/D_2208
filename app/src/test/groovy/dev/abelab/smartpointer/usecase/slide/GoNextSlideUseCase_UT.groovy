package dev.abelab.smartpointer.usecase.slide

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.enums.SlideControl
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * GoNextSlideUseCaseの単体テスト
 */
class GoNextSlideUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GoNextSlideUseCase sut

    def "handle: スライドを進める"() {
        given:
        final room = RandomHelper.mock(RoomModel)

        when:
        final result = this.sut.handle(room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        result.roomId == room.id
        result.slideControl == SlideControl.NEXT
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
