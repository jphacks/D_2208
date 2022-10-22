package dev.abelab.smartpointer.infrastructure.factory;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.infrastructure.db.entity.User;

/**
 * ユーザファクトリ
 */
@Component
public class UserFactory {

    /**
     * Userを作成
     *
     * @param userModel model
     * @return entity
     */
    public User createUser(final UserModel userModel) {
        return User.builder() //
            .id(userModel.getId()) //
            .roomId(userModel.getRoomId()) //
            .name(userModel.getName()) //
            .build();
    }

}
