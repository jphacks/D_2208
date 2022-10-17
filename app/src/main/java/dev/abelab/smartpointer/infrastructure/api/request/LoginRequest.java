package dev.abelab.smartpointer.infrastructure.api.request;

import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.util.ValidationUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ログインリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements BaseRequest {

    /**
     * ルームID
     */
    @Schema(required = true)
    String roomId;

    /**
     * トークン
     */
    @Schema(required = true)
    String token;

    /**
     * ユーザ名
     */
    @Schema(required = true)
    String name;

    /**
     * バリデーション
     */
    public void validate() {
        // ユーザ名
        if (!ValidationUtil.checkStringLength(this.getName(), 1, 255)) {
            throw new BadRequestException(ErrorCode.INVALID_USER_NAME);
        }
    }

}
