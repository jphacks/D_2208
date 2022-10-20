package dev.abelab.smartpointer.infrastructure.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(required = true)
    Double alpha;

    /**
     * β値
     */
    @Schema(required = true)
    Double beta;

    /**
     * γ値
     */
    @Schema(required = true)
    Double gamma;

    /**
     * バリデーション
     */
    public void validate() {}

}
