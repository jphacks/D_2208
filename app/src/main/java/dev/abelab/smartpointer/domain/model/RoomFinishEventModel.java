package dev.abelab.smartpointer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルーム終了イベントモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomFinishEventModel {

    /**
     * ルームID
     */
    String roomId;

}
