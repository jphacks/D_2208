package dev.abelab.smartpointer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ポインター操作モデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointerControlModel {

    /**
     * ルームID
     */
    String roomId;

    /**
     * 操作者
     */
    UserModel user;

    /**
     * ポインター操作方向
     */
    PointerControlOrientationModel orientation;

    public PointerControlModel(final String roomId, final UserModel user, final Double alpha, final Double beta, final Double gamma) {
        this.roomId = roomId;
        this.user = user;
        this.orientation = new PointerControlOrientationModel(alpha, beta, gamma);
    }

}
