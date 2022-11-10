package dev.abelab.smartpointer.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

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
     * 入力時間 [s]
     */
    Integer inputTime;

    /**
     * 一時停止時点での残り時間 [s]
     */
    @Builder.Default
    Optional<Integer> remainingTimeAtPaused = Optional.empty();

    /**
     * 終了時刻
     */
    LocalDateTime finishAt;

    public TimerModel(final Timer timer) {
        this.roomId = timer.getRoomId();
        this.status = TimerStatus.find(timer.getStatus()) //
            .orElseThrow(() -> new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
        this.inputTime = timer.getInputTime();
        this.remainingTimeAtPaused = Optional.ofNullable(timer.getRemainingTimeAtPaused());
        this.finishAt = timer.getFinishAt();
    }

    /**
     * タイマーを開始
     * 
     * @param inputTime 入力時間
     */
    public void start(final Integer inputTime) {
        if (!this.getStatus().equals(TimerStatus.READY)) {
            throw new BadRequestException(ErrorCode.TIMER_CANNOT_BE_STARTED);
        }

        this.setStatus(TimerStatus.RUNNING);
        this.setInputTime(inputTime);
        this.setRemainingTimeAtPaused(Optional.empty());
        this.setFinishAt(LocalDateTime.now().plusSeconds(inputTime));
    }

    /**
     * タイマーを再開
     * 
     * @param value タイマー時間[s]
     */
    public void resume(final Integer value) {
        if (!this.getStatus().equals(TimerStatus.READY)) {
            throw new BadRequestException(ErrorCode.TIMER_CANNOT_BE_STARTED);
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

    /**
     * タイマーをリセット
     */
    public void reset() {
        if (!this.getStatus().equals(TimerStatus.READY)) {
            throw new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESET);
        }

        this.setFinishAt(LocalDateTime.now().plusSeconds(this.getInputTime()));
    }

}
