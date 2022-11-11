package dev.abelab.smartpointer.infrastructure.api.type;

import dev.abelab.smartpointer.domain.model.CustomPointerModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カスタムポインター
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPointer {

    /**
     * カスタムポインターID
     */
    String id;

    /**
     * ラベル
     */
    String label;

    /**
     * URL
     */
    String url;

    public CustomPointer(final CustomPointerModel customPointerModel) {
        this.id = customPointerModel.getId();
        this.label = customPointerModel.getLabel();
        this.url = customPointerModel.getUrl();
    }

}
