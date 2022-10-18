package dev.abelab.smartpointer.infrastructure.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(required = true)
    String tokenType;

    /**
     * アクセストークン
     */
    @Schema(required = true)
    String accessToken;

}
