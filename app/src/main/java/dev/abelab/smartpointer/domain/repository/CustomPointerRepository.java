package dev.abelab.smartpointer.domain.repository;

import java.util.List;

import dev.abelab.smartpointer.domain.model.CustomPointerModel;

/**
 * カスタムポインターリポジトリ
 */
public interface CustomPointerRepository {

    /**
     * ルームIDからカスタムポインターリストを取得
     * 
     * @param roomId ルームID
     * @return カスタムポインターリスト
     */
    List<CustomPointerModel> selectByRoomId(final String roomId);

}
