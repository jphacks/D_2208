package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest;
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
    Double alpha;

    /**
     * β値
     */
    Double beta;

    /**
     * γ値
     */
    Double gamma;

    public PointerRotationResponse(final PointerControlRequest requestBody) {
        this.alpha = requestBody.getAlpha();
        this.beta = requestBody.getBeta();
        this.gamma = requestBody.getGamma();
    }

}
