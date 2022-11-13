package dev.abelab.smartpointer.domain.model;

import dev.abelab.smartpointer.enums.SlideControl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * スライド操作モデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlideControlModel {

    /**
     * ルームID
     */
    String roomId;

    /**
     * スライド操作
     */
    SlideControl slideControl;

}
