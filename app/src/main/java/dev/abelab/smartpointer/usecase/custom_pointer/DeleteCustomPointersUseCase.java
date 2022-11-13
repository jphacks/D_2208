package dev.abelab.smartpointer.usecase.custom_pointer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.repository.CustomPointerRepository;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * カスタムポインター削除ユースケース
 */
@RequiredArgsConstructor
@Component
public class DeleteCustomPointersUseCase {

    private final RoomRepository roomRepository;

    private final CustomPointerRepository customPointerRepository;

    /**
     * Handle UseCase
     *
     * @param id カスタムポインターID
     * @param roomId ルームID
     */
    @Transactional
    public void handle(final String id, final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // カスタムポインターの存在チェック & 削除
        if (!this.customPointerRepository.existsByIdAndRoomId(id, roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_CUSTOM_POINTER);
        }
        this.customPointerRepository.deleteByIdAndRoomId(id, roomId);
    }

}
