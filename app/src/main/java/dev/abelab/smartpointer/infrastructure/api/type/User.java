package dev.abelab.smartpointer.infrastructure.api.type;

import dev.abelab.smartpointer.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザ
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * ユーザID
     */
    String id;

    /**
     * ユーザ名
     */
    String name;

    public User(final UserModel userModel) {
        this.id = userModel.getId();
        this.name = userModel.getName();
    }

}
