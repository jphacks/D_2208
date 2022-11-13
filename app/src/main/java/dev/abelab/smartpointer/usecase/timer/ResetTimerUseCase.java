package dev.abelab.smartpointer.usecase.timer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * タイマーリセットユースケース
 */
@RequiredArgsConstructor
@Component
public class ResetTimerUseCase {

    private final RoomRepository roomRepository;

    private final TimerRepository timerRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @return タイマー
     */
    @Transactional
    public TimerModel handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // タイマーを取得
        final var timer = this.timerRepository.selectByRoomId(roomId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TIMER));

        // タイマーを更新
        timer.reset();
        this.timerRepository.upsert(timer);

        return timer;
    }

}
