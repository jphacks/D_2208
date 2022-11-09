package dev.abelab.smartpointer.usecase.user;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.UserRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * ユーザリスト取得ユースケース
 */
@RequiredArgsConstructor
@Component
public class GetUsersUseCase {

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    /**
     * Handle UseCase
     *
     * @param roomId ルームID
     * @return ユーザリスト
     */
    @Transactional
    public List<UserModel> handle(final String roomId) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        return this.userRepository.selectByRoomId(roomId);
    }

}
