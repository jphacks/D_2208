package dev.abelab.smartpointer.infrastructure.factory;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.infrastructure.db.entity.Timer;

/**
 * タイマーファクトリ
 */
@Component
public class TimerFactory {

    /**
     * Timerを作成
     *
     * @param timerModel model
     * @return entity
     */
    public Timer createTimer(final TimerModel timerModel) {
        return Timer.builder() //
            .roomId(timerModel.getRoomId()) //
            .status(timerModel.getStatus().getId()) //
            .inputTime(timerModel.getInputTime()) //
            .remainingTimeAtPaused(timerModel.getRemainingTimeAtPaused().orElse(null)) //
            .finishAt(timerModel.getFinishAt()) //
            .build();
    }

}
