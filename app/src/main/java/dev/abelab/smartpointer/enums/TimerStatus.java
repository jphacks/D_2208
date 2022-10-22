package dev.abelab.smartpointer.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * タイマーステータス
 */
@Getter
@AllArgsConstructor
public enum TimerStatus {

    /**
     * 準備中
     */
    READY(0),

    /**
     * 実行中
     */
    RUNNING(1);

    /**
     * タイマーステータスID
     */
    private final Integer id;

    /**
     * タイマーステータスを検索
     *
     * @param id タイマーステータスID
     * @return タイマーステータス
     */
    public static Optional<TimerStatus> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst();
    }

}
