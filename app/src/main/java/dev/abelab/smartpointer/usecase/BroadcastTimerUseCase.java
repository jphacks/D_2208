package dev.abelab.smartpointer.usecase;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.infrastructure.api.response.TimerResponse;
import lombok.RequiredArgsConstructor;

/**
 * タイマー取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class BroadcastTimerUseCase {

    private final RoomRepository roomRepository;

    private final TimerRepository timerRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     */
    @Transactional
    public void handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // タイマーを取得
        final var timer = this.timerRepository.selectByRoomId(roomId) //
            .orElse(TimerModel.builder().roomId(roomId).build());

        // タイマーを配信
        this.simpMessagingTemplate.convertAndSend(String.format("/topic/rooms/%s/timer", roomId), new TimerResponse(timer));
    }

}
