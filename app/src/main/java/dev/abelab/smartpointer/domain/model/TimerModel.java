package dev.abelab.smartpointer.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import dev.abelab.smartpointer.enums.TimerStatus;
import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.InternalServerErrorException;
import dev.abelab.smartpointer.infrastructure.db.entity.Timer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * タイマーモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimerModel implements Serializable {

    /**
     * ルームID
     */
    String roomId;

    /**
     * ステータス
     */
    @Builder.Default
    TimerStatus status = TimerStatus.READY;

    /**
     * 初期値[s]
     */
    Integer value;

    /**
     * 終了日時
     */
    LocalDateTime finishAt;

    public TimerModel(final Timer timer) {
        this.roomId = timer.getRoomId();
        this.status = TimerStatus.find(timer.getStatus()) //
            .orElseThrow(() -> new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
        this.value = timer.getValue();
        this.finishAt = timer.getFinishAt();
    }

    /**
     * タイマーを開始
     * 
     * @param value タイマー時間[s]
     */
    public void start(final Integer value) {
        if (!this.getStatus().equals(TimerStatus.READY)) {
            throw new BadRequestException(ErrorCode.TIMER_IS_ALREADY_STARTED);
        }

        this.setStatus(TimerStatus.RUNNING);
        this.setValue(value);
        this.setFinishAt(LocalDateTime.now().plusSeconds(value));
    }

    /**
     * タイマーを再開
     * 
     * @param value タイマー時間[s]
     */
    public void restart(final Integer value) {
        if (!this.getStatus().equals(TimerStatus.READY)) {
            throw new BadRequestException(ErrorCode.TIMER_IS_ALREADY_STARTED);
        }

        this.setStatus(TimerStatus.RUNNING);
        this.setFinishAt(LocalDateTime.now().plusSeconds(value));
    }

    /**
     * タイマーを停止
     */
    public void stop() {
        if (!this.getStatus().equals(TimerStatus.RUNNING)) {
            throw new BadRequestException(ErrorCode.TIMER_IS_ALREADY_STOPPED);
        }

        this.setStatus(TimerStatus.READY);
    }

}
