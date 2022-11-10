package dev.abelab.smartpointer.infrastructure.api.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * エラー
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    /**
     * エラーメッセージ
     */
    String message;

    /**
     * エラーコード
     */
    Integer code;

}
