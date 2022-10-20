package dev.abelab.smartpointer.infrastructure.api.response;

import dev.abelab.smartpointer.enums.SlideControl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * スライド操作レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideControlResponse {

    /**
     * スライド操作ID
     */
    @Schema(required = true)
    Integer control;

    public SlideControlResponse(final SlideControl slideControl) {
        this.control = slideControl.getId();
    }

}
