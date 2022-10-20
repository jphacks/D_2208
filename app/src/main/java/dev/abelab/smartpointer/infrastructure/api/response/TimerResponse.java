package dev.abelab.smartpointer.infrastructure.api.response;

import java.time.LocalDateTime;

import dev.abelab.smartpointer.domain.model.TimerModel;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(required = true)
    Integer status;

    /**
     * 初期値[s]
     */
    @Schema(required = true, nullable = true)
    Integer value;

    /**
     * 終了日時
     */
    @Schema(required = true, nullable = true)
    LocalDateTime finishAt;

    public TimerResponse(final TimerModel timerModel) {
        this.status = timerModel.getStatus().getId();
        this.value = timerModel.getValue();
        this.finishAt = timerModel.getFinishAt();
    }

}
