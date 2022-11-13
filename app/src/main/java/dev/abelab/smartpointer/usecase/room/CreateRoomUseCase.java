package dev.abelab.smartpointer.usecase.room;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.RoomModel;
import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.enums.TimerStatus;
import lombok.RequiredArgsConstructor;

/**
 * ルーム作成ユースケース
 */
@RequiredArgsConstructor
@Component
public class CreateRoomUseCase {

    private final RoomRepository roomRepository;

    private final TimerRepository timerRepository;

    /**
     * Handle UseCase
     * 
     * @param pointerType ポインタータイプ
     * @return ルーム
     */
    @Transactional
    public RoomModel handle(final String pointerType) {
        // ルームを作成
        final var room = RoomModel.builder() //
            .pointerType(pointerType) //
            .build();
        this.roomRepository.insert(room);

        // ルームを作成
        final var defaultInputValue = 300;
        final var timer = TimerModel.builder() //
            .roomId(room.getId()) //
            .inputTime(defaultInputValue) //
            .finishAt(LocalDateTime.now().plusSeconds(defaultInputValue)) //
            .status(TimerStatus.READY) //
            .build();
        this.timerRepository.insert(timer);

        return room;
    }

}
