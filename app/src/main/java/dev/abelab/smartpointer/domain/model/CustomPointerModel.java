package dev.abelab.smartpointer.domain.model;

import dev.abelab.smartpointer.infrastructure.db.entity.CustomPointer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カスタムポインターモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPointerModel {

    /**
     * カスタムポインターID
     */
    String id;

    /**
     * ルームID
     */
    String roomId;

    /**
     * ラベル
     */
    String label;

    /**
     * URL
     */
    String url;

    public CustomPointerModel(final CustomPointer customPointer) {
        this.id = customPointer.getId();
        this.roomId = customPointer.getRoomId();
        this.label = customPointer.getLabel();
        this.url = customPointer.getUrl();
    }

}
