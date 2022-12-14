package dev.abelab.smartpointer.domain.service;

import org.springframework.stereotype.Service;

import dev.abelab.smartpointer.domain.repository.UserRepository;
import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ConflictException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.util.ValidationUtil;
import lombok.RequiredArgsConstructor;

/**
 * ユーザサービス
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * ユーザ名が既に使われているかチェック
     * 
     * @param roomId ルームID
     * @param name ユーザ名
     */
    public void checkIsNameAlreadyUsed(final String roomId, final String name) throws ConflictException {
        if (this.userRepository.existsByRoomIdAndName(roomId, name)) {
            throw new ConflictException(ErrorCode.USER_NAME_IS_ALREADY_EXISTS);
        }
    }

    /**
     * ユーザ名が有効かチェック
     * 
     * @param name ユーザ名
     */
    public void checkIsNameValid(final String name) throws BadRequestException {
        if (!ValidationUtil.checkStringLength(name, 1, 255)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_NAME);
        }
    }

}
