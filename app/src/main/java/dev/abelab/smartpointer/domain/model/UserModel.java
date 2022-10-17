package dev.abelab.smartpointer.domain.model;

import java.io.Serializable;
import java.util.UUID;

import dev.abelab.smartpointer.infrastructure.db.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel implements Serializable {

    /**
     * ユーザID
     */
    @Builder.Default
    String id = UUID.randomUUID().toString();

    /**
     * ルームID
     */
    String roomId;

    /**
     * ユーザ名
     */
    String name;

    public UserModel(final User user) {
        this.id = user.getId();
        this.roomId = user.getRoomId();
        this.name = user.getName();
    }

}
