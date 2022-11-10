package dev.abelab.smartpointer.infrastructure.api.request;

import dev.abelab.smartpointer.util.JsonMessageUtil;
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
    Integer value;

    public TimerStartRequest(final String payload) {
        final var object = JsonMessageUtil.convertJsonToObject(payload, TimerStartRequest.class);
        this.value = object.getValue();
    }

    /**
     * バリデーション
     */
    @Override
    public void validate() {}

}
