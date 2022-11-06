package dev.abelab.smartpointer.infrastructure.api.request;

import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.util.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルーム入室リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomJoinRequest implements BaseRequest {

    /**
     * パスコード
     */
    String passcode;

    /**
     * ユーザ名
     */
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
