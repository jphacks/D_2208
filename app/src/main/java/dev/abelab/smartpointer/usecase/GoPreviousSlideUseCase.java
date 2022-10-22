package dev.abelab.smartpointer.usecase;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.enums.SlideControl;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.infrastructure.api.response.SlideControlResponse;
import lombok.RequiredArgsConstructor;

/**
 * スライドを戻すユースケース
 */
@RequiredArgsConstructor
@Component
public class GoPreviousSlideUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @return スライド操作レスポンス
     */
    @Transactional
    public SlideControlResponse handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return new SlideControlResponse(SlideControl.PREVIOUS);
    }

}
