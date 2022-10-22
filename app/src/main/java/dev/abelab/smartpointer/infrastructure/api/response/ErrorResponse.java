package dev.abelab.smartpointer.infrastructure.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * エラーレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * エラーメッセージ
     */
    @Schema(required = true)
    String message;

    /**
     * エラーコード
     */
    @Schema(required = true)
    Integer code;

}
