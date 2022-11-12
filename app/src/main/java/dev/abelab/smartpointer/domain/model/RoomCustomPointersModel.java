package dev.abelab.smartpointer.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルームカスタムポインターリストモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomCustomPointersModel {

    /**
     * ルームID
     */
    String roomId;

    /**
     * カスタムポインターリスト
     */
    List<CustomPointerModel> customPointers;

}
