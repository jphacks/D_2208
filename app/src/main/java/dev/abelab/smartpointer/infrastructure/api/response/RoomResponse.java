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
    String id;

    /**
     * パスコード
     */
    String passcode;

    public RoomResponse(final RoomModel roomModel) {
        this.id = roomModel.getId();
        this.passcode = roomModel.getPasscode();
    }

}
