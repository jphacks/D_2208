package dev.abelab.smartpointer.infrastructure.api.type;

import dev.abelab.smartpointer.domain.model.RoomModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルーム
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    /**
     * ルームID
     */
    String id;

    /**
     * パスコード
     */
    String passcode;

    /**
     * ポインタータイプ
     */
    String pointerType;

    public Room(final RoomModel roomModel) {
        this.id = roomModel.getId();
        this.passcode = roomModel.getPasscode();
        this.pointerType = roomModel.getPointerType();
    }

}
