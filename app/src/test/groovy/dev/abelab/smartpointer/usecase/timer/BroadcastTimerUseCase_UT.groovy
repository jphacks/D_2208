package dev.abelab.smartpointer.usecase.timer

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.domain.model.TimerModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.usecase.AbstractUseCase_UT
import org.springframework.beans.factory.annotation.Autowired

/**
 * BroadcastTimerUseCaseUseCaseの単体テスト
 */
class BroadcastTimerUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    BroadcastTimerUseCase sut

    def "handle: タイマーを配信する"() {
        given:
        final timer = RandomHelper.mock(TimerModel)
        final room = RandomHelper.mock(RoomModel)

        when:
        this.sut.handle(room.id)

        then:
        1 * this.roomRepository.existsById(room.id) >> true
        1 * this.timerRepository.selectByRoomId(room.id) >> Optional.of(timer)
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
