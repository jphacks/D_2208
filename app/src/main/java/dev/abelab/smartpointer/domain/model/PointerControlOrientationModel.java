package dev.abelab.smartpointer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ポインター操作方向モデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointerControlOrientationModel {

    /**
     * α値
     */
    Double alpha;

    /**
     * β値
     */
    Double beta;

    /**
     * γ値
     */
    Double gamma;

}
