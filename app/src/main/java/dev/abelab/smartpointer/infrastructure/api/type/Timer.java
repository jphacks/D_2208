package dev.abelab.smartpointer.infrastructure.api.type;

import java.time.LocalDateTime;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.enums.TimerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイマー
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timer {

    /**
     * 入力時間 [s]
     */
    Integer inputTime;

    /**
     * 一時停止時点での残り時間 [s]
     */
    Integer remainingTimeAtPaused;

    /**
     * 終了時刻
     */
    LocalDateTime finishAt;

    /**
     * ステータス
     */
    TimerStatus status;

    public Timer(final TimerModel timerModel) {
        this.inputTime = timerModel.getInputTime();
        this.remainingTimeAtPaused = timerModel.getRemainingTimeAtPaused().orElse(null);
        this.finishAt = timerModel.getFinishAt();
        this.status = timerModel.getStatus();
    }

}
