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
 * GoPreviousSlideUseCaseの単体テスト
 */
class GoPreviousSlideUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    GoPreviousSlideUseCase sut

    def "handle: スライドを戻す"() {
        given:
        final room = RandomHelper.mock(RoomModel)

        when:
        final result = this.sut.handle(room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        result == SlideControl.PREVIOUS
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
