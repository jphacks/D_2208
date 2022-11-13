package dev.abelab.smartpointer.usecase.pointer;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
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
     * @param loginUser ログインユーザ
     * @return ユーザ
     */
    public UserModel handle(final String roomId, final UserModel loginUser) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return loginUser;
    }

}
