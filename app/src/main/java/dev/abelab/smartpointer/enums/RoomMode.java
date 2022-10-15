package dev.abelab.smartpointer.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ルームモード
 */
@Getter
@AllArgsConstructor
public enum RoomMode {

    /**
     * プレゼンテーションモード
     */
    PRESENTATION(0),

    /**
     * Web会議モード
     */
    WEB_MEETING(1);

    /**
     * ルームモードID
     */
    private final Integer id;

    /**
     * ルームモードを検索
     *
     * @param id ルームモードID
     * @return ルームモード
     */
    public static Optional<RoomMode> find(final Integer id) {
        return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst();
    }

}
