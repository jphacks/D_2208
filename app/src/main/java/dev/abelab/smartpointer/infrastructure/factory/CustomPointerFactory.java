package dev.abelab.smartpointer.infrastructure.factory;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.CustomPointerModel;
import dev.abelab.smartpointer.infrastructure.db.entity.CustomPointer;

/**
 * カスタムポインターファクトリ
 */
@Component
public class CustomPointerFactory {

    /**
     * CustomPointerを作成
     *
     * @param customPointerModel model
     * @return entity
     */
    public CustomPointer createCustomPointer(final CustomPointerModel customPointerModel) {
        return CustomPointer.builder() //
            .id(customPointerModel.getId()) //
            .roomId(customPointerModel.getRoomId()) //
            .label(customPointerModel.getLabel()) //
            .url(customPointerModel.getUrl()) //
            .build();
    }

}
