package dev.abelab.smartpointer.usecase.pointer;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ポインタータイプ変更ユースケース
 */
@RequiredArgsConstructor
@Component
public class ChangePointerTypeUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     * 
     * @param pointerType ポインタータイプ
     * @param roomId ルームID
     */
    public void handle(final String pointerType, final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // ポインタータイプを変更
        this.roomRepository.updatePointerTypeById(roomId, pointerType);
    }

}
