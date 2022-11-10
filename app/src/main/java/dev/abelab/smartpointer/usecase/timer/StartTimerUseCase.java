package dev.abelab.smartpointer.usecase.timer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.domain.service.TimerService;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * タイマー開始ユースケース
 */
@RequiredArgsConstructor
@Component
public class StartTimerUseCase {

    private final RoomRepository roomRepository;

    private final TimerRepository timerRepository;

    private final TimerService timerService;

    /**
     * Handle UseCase
     *
     * @param roomId ルームID
     * @param inputTime 入力時間
     * @return タイマー
     */
    @Transactional
    public TimerModel handle(final String roomId, final Integer inputTime) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // 入力時間チェック
        this.timerService.checkIsInputTimeValid(inputTime);

        // タイマーを取得
        final var timer = this.timerRepository.selectByRoomId(roomId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TIMER));

        // タイマーを更新
        timer.start(inputTime);
        this.timerRepository.upsert(timer);

        return timer;
    }

}
