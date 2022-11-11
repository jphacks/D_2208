package dev.abelab.smartpointer.infrastructure.api.type;

import dev.abelab.smartpointer.domain.model.PointerControlModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ポインター操作
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointerControl {

    /**
     * 操作者
     */
    User user;

    /**
     * ポインター操作方向
     */
    PointerControlOrientation orientation;

    public PointerControl(final PointerControlModel pointerControlModel) {
        this.user = new User(pointerControlModel.getUser());
        this.orientation = new PointerControlOrientation(pointerControlModel.getOrientation());
    }

}
