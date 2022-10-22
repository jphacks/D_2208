package dev.abelab.smartpointer.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.infrastructure.api.response.PointerControlResponse;
import lombok.RequiredArgsConstructor;

/**
 * ポインター切断ユースケース
 */
@RequiredArgsConstructor
@Component
public class DisconnectPointerUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @return ポインター操作レスポンス
     */
    @Transactional
    public PointerControlResponse handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return PointerControlResponse.builder() //
            .rotation(null) //
            .isActive(false) //
            .build();
    }

}
