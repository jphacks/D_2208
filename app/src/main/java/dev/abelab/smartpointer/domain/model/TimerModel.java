package dev.abelab.smartpointer.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

        // DB上はRUNNINGだが終了時刻を過ぎている場合があるので、終了時刻を過ぎていたらREADYにする
        if (LocalDateTime.now().isAfter(this.finishAt)) {
            this.status = TimerStatus.READY;
            this.remainingTimeAtPaused = Optional.empty();
        }
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
     */
    public void resume() {
        if (!this.getStatus().equals(TimerStatus.PAUSED)) {
            throw new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESUMED);
        }

        this.setFinishAt(LocalDateTime.now().plusSeconds(this.remainingTimeAtPaused.orElse(0)));
        this.setStatus(TimerStatus.RUNNING);
        this.setRemainingTimeAtPaused(Optional.empty());
    }

    /**
     * タイマーを一時停止
     */
    public void pause() {
        if (!this.getStatus().equals(TimerStatus.RUNNING)) {
            throw new BadRequestException(ErrorCode.TIMER_CANNOT_BE_PAUSED);
        }

        // レイテンシによっては残り時間が0sを下回る可能性があるが、とりあえず考慮しない
        this.setRemainingTimeAtPaused(Optional.of(Math.toIntExact(ChronoUnit.SECONDS.between(LocalDateTime.now(), this.finishAt))));
        this.setStatus(TimerStatus.PAUSED);
    }

    /**
     * タイマーをリセット
     */
    public void reset() {
        if (this.getStatus().equals(TimerStatus.READY)) {
            throw new BadRequestException(ErrorCode.TIMER_CANNOT_BE_RESET);
        }

        this.setRemainingTimeAtPaused(Optional.empty());
        this.setFinishAt(LocalDateTime.now().plusSeconds(this.getInputTime()));
        this.setStatus(TimerStatus.READY);
    }

}
