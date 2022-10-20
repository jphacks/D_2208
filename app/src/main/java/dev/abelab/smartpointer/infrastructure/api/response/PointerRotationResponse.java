package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ポインター回転数レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointerRotationResponse {

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

    public PointerRotationResponse(final PointerControlRequest requestBody) {
        this.alpha = requestBody.getAlpha();
        this.beta = requestBody.getBeta();
        this.gamma = requestBody.getGamma();
    }

}
