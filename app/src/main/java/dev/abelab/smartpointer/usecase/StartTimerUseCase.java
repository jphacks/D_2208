package dev.abelab.smartpointer.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.enums.TimerStatus;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.infrastructure.api.request.TimerStartRequest;
import lombok.RequiredArgsConstructor;

/**
 * タイマー開始ユースケース
 */
@RequiredArgsConstructor
@Component
public class StartTimerUseCase {

    private final RoomRepository roomRepository;

    private final TimerRepository timerRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @param requestBody タイマー開始リクエスト
     */
    @Transactional
    public void handle(final String roomId, final TimerStartRequest requestBody) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // タイマーを取得
        final var timer = this.timerRepository.selectByRoomId(roomId) //
            .orElse(TimerModel.builder().status(TimerStatus.READY).roomId(roomId).build());

        // タイマーを更新
        timer.start(requestBody.getValue());
        this.timerRepository.upsert(timer);
    }

}
