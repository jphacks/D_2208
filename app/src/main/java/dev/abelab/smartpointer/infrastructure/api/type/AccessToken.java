package dev.abelab.smartpointer.infrastructure.api.type;

import dev.abelab.smartpointer.domain.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アクセストークン
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

    /**
     * トークンタイプ
     */
    String tokenType;

    /**
     * アクセストークン
     */
    String accessToken;

    /**
     * TTL
     */
    Integer ttl;

    /**
     * ユーザ
     */
    User user;

    public AccessToken(final String tokenType, final String accessToken, final Integer ttl, final UserModel userModel) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.ttl = ttl;
        this.user = new User(userModel);
    }

}
