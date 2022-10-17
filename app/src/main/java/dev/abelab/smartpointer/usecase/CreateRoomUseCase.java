package dev.abelab.smartpointer.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.RoomModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

/**
 * ルーム作成ユースケース
 */
@RequiredArgsConstructor
@Component
public class CreateRoomUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     * 
     * @return ルーム
     */
    @Transactional
    public RoomModel handle() {
        // ルームを作成
        final var room = RoomModel.builder().build();
        this.roomRepository.insert(room);

        return room;
    }

}
