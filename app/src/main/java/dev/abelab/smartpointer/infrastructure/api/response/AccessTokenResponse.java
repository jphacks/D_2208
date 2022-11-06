package dev.abelab.smartpointer.infrastructure.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アクセストークンレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponse {

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
