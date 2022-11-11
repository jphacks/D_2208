package dev.abelab.smartpointer.infrastructure.api.type;

import java.util.List;

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

}
