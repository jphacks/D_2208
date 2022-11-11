package dev.abelab.smartpointer.usecase.custom_pointer;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.CustomPointerModel;
import dev.abelab.smartpointer.domain.repository.CustomPointerRepository;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * カスタムポインターリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetCustomPointersUseCase {

    private final RoomRepository roomRepository;

    private final CustomPointerRepository customPointerRepository;

    /**
     * Handle UseCase
     *
     * @param roomId ルームID
     * @return カスタムポインターリスト
     */
    @Transactional
    public List<CustomPointerModel> handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return this.customPointerRepository.selectByRoomId(roomId);
    }

}
