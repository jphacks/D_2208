package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.domain.model.RoomModel;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(required = true)
    String roomId;

    /**
     * トークン
     */
    @Schema(required = true)
    String token;

    public RoomResponse(final RoomModel roomModel) {
        this.roomId = roomModel.getId();
        this.token = roomModel.getToken();
    }

}
