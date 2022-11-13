package dev.abelab.smartpointer.usecase.pointer;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ポインタータイプ取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetPointerTypeUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     *
     * @param roomId ルームID
     * @return ポインタータイプ
     */
    public String handle(final String roomId) {
        // ルームの取得 & 存在チェック
        final var room = this.roomRepository.selectById(roomId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ROOM));

        return room.getPointerType();
    }

}
