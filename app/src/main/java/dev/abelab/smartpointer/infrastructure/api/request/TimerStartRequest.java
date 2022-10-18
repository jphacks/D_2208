package dev.abelab.smartpointer.infrastructure.api.request;

import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.util.JsonMessageUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイマー開始リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimerStartRequest implements BaseRequest {

    /**
     * 設定時間[s]
     */
    @Schema(required = true)
    Integer value;

    public TimerStartRequest(final String payload) {
        final var object = JsonMessageUtil.convertJsonToObject(payload, TimerStartRequest.class);
        this.value = object.getValue();
    }

    /**
     * バリデーション
     */
    public void validate() {
        // 設定時間
        if (this.getValue() < 1 || this.getValue() > 3600) {
            throw new BadRequestException(ErrorCode.INVALID_TIMER_VALUE);
        }
    }

}
