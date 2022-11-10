package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest;
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
public class PointerControlResponse {

    /**
     * 回転数
     */
    PointerRotationResponse rotation;

    /**
     * ポインターがアクティブかどうか
     */
    Boolean isActive;

    public PointerControlResponse(final PointerControlRequest requestBody, final Boolean isActive) {
        this.rotation = new PointerRotationResponse(requestBody);
        this.isActive = isActive;
    }

}
