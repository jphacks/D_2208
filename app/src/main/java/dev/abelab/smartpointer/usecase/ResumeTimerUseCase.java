package dev.abelab.smartpointer.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.infrastructure.api.request.TimerResumeRequest;
import lombok.RequiredArgsConstructor;

/**
 * タイマー再開ユースケース
 */
@RequiredArgsConstructor
@Component
public class ResumeTimerUseCase {

    private final RoomRepository roomRepository;

    private final TimerRepository timerRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @param requestBody タイマー再開リクエスト
     */
    @Transactional
    public void handle(final String roomId, final TimerResumeRequest requestBody) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // タイマーを取得
        final var timer = this.timerRepository.selectByRoomId(roomId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TIMER));

        // タイマーを再開
        timer.resume(requestBody.getValue());
        this.timerRepository.upsert(timer);
    }

}
