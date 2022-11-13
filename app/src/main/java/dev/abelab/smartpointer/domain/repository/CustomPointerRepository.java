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

    /**
     * ID、ルームIDからカスタムポインターの存在チェック
     * 
     * @param id カスタムポインターID
     * @param roomId ルームID
     * @return チェック結果
     */
    boolean existsByIdAndRoomId(final String id, final String roomId);

    /**
     * カスタムポインターを作成
     * 
     * @param customPointerModel カスタムポインター
     */
    void insert(final CustomPointerModel customPointerModel);

    /**
     * ID、ルームIDからカスタムポインターを削除
     *
     * @param id カスタムポインターID
     * @param roomId ルームID
     */
    void deleteByIdAndRoomId(final String id, final String roomId);

}
