package dev.abelab.smartpointer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルームポインターモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomPointerModel {

    /**
     * ルームID
     */
    String roomId;

    /**
     * ポインタータイプ
     */
    String pointerType;

}
