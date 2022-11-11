package dev.abelab.smartpointer.usecase.slide;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.SlideControlModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.enums.SlideControl;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * スライドを進めるユースケース
 */
@RequiredArgsConstructor
@Component
public class GoNextSlideUseCase {

    private final RoomRepository roomRepository;

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @return スライド操作
     */
    @Transactional
    public SlideControlModel handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return new SlideControlModel(roomId, SlideControl.NEXT);
    }

}
