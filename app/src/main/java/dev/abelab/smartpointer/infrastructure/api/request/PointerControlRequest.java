package dev.abelab.smartpointer.infrastructure.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ポインター操作リクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointerControlRequest implements BaseRequest {

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

    /**
     * バリデーション
     */
    public void validate() {}

}
