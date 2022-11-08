package dev.abelab.smartpointer.usecase.pointer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest;
import dev.abelab.smartpointer.infrastructure.api.response.PointerControlResponse;
import lombok.RequiredArgsConstructor;

/**
 * ポインター操作ユースケース
 */
@RequiredArgsConstructor
@Component
public class ControlPointerUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @param requestBody ポインター操作リクエスト
     * @return ポインター操作レスポンス
     */
    @Transactional
    public PointerControlResponse handle(final String roomId, final PointerControlRequest requestBody) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return new PointerControlResponse(requestBody, true);
    }

}
