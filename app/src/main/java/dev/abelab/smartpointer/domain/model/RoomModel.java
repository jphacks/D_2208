package dev.abelab.smartpointer.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

import dev.abelab.smartpointer.infrastructure.db.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルームモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomModel {

    /**
     * ルームID
     */
    @Builder.Default
    String id = String.format("%s-%s-%s", //
        RandomStringUtils.randomAlphanumeric(3), //
        RandomStringUtils.randomAlphanumeric(3), //
        RandomStringUtils.randomAlphanumeric(3) //
    );

    /**
     * パスコード
     */
    @Builder.Default
    String passcode = RandomStringUtils.randomNumeric(6);

    public RoomModel(final Room room) {
        this.id = room.getId();
        this.passcode = room.getPasscode();
    }

    /**
     * パスコードが有効かチェック
     * 
     * @param passcode パスコード
     * @return チェック結果
     */
    public boolean isPasscodeValid(final String passcode) {
        return this.passcode.equals(passcode);
    }

}
