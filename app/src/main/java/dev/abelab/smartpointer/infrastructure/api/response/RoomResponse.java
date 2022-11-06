package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.domain.model.RoomModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルームレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {

    /**
     * ルームID
     */
    String roomId;

    /**
     * パスコード
     */
    String passcode;

    public RoomResponse(final RoomModel roomModel) {
        this.roomId = roomModel.getId();
        this.passcode = roomModel.getPasscode();
    }

}
