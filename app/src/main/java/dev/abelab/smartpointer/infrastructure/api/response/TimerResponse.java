package dev.abelab.smartpointer.infrastructure.api.response;

import java.time.LocalDateTime;

import dev.abelab.smartpointer.domain.model.TimerModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイマーレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimerResponse {

    /**
     * ステータス
     */
    Integer status;

    /**
     * 初期値[s]
     */
    Integer value;

    /**
     * 終了日時
     */
    LocalDateTime finishAt;

    public TimerResponse(final TimerModel timerModel) {
        this.status = timerModel.getStatus().getId();
        this.value = timerModel.getValue();
        this.finishAt = timerModel.getFinishAt();
    }

}
