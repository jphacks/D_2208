package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ポインター操作レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointerControlResponse {

    /**
     * 回転数
     */
    @Schema(required = true, nullable = true)
    PointerRotationResponse rotation;

    /**
     * ポインターがアクティブかどうか
     */
    @Schema(required = true)
    Boolean isActive;

    // TODO: ポインター操作者を入れる

    public PointerControlResponse(final PointerControlRequest requestBody, final Boolean isActive) {
        this.rotation = new PointerRotationResponse(requestBody);
        this.isActive = isActive;
    }

}
