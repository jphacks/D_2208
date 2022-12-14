package dev.abelab.smartpointer.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * スライド操作
 */
@Getter
@AllArgsConstructor
public enum SlideControl {

    /**
     * 進める
     */
    NEXT(0),

    /**
     * 戻す
     */
    PREVIOUS(1);

    /**
     * スライド操作ID
     */
    private final Integer id;

    /**
     * スライド操作を検索
     *
     * @param id タイマーステータスID
     * @return タイマーステータス
     */
    public static Optional<SlideControl> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst();
    }

}
