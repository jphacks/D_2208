package dev.abelab.smartpointer.infrastructure.api.type;

import java.util.List;
import java.util.stream.Collectors;

import dev.abelab.smartpointer.domain.model.RoomCustomPointersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カスタムポインターリスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPointers {

    /**
     * カスタムポインターリスト
     */
    List<CustomPointer> customPointers;

    public CustomPointers(final RoomCustomPointersModel roomCustomPointersModel) {
        this.customPointers = roomCustomPointersModel.getCustomPointers().stream() //
            .map(CustomPointer::new) //
            .collect(Collectors.toList());
    }

}
