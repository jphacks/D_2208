package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * ユーザID
     */
    String id;

    /**
     * ユーザ名
     */
    String name;

    public UserResponse(final UserModel userModel) {
        this.id = userModel.getId();
        this.name = userModel.getName();
    }

}
