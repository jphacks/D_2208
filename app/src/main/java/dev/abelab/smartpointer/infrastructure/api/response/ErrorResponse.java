package dev.abelab.smartpointer.infrastructure.api.response;

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
    String message;

    /**
     * エラーコード
     */
    Integer code;

}
