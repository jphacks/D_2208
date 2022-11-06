package dev.abelab.smartpointer.infrastructure.api.request;

import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.util.JsonMessageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイマー再開リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimerResumeRequest implements BaseRequest {

    /**
     * 残り時間[s]
     */
    Integer value;

    public TimerResumeRequest(final String payload) {
        final var object = JsonMessageUtil.convertJsonToObject(payload, TimerResumeRequest.class);
        this.value = object.getValue();
    }

    /**
     * バリデーション
     */
    public void validate() {
        // 残り時間
        if (this.getValue() < 1 || this.getValue() > 3600) {
            throw new BadRequestException(ErrorCode.INVALID_TIMER_VALUE);
        }
    }

}
