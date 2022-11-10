package dev.abelab.smartpointer.infrastructure.api.type;

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

}
