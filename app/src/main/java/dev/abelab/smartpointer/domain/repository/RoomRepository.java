package dev.abelab.smartpointer.domain.repository;

import java.util.Optional;

import dev.abelab.smartpointer.domain.model.RoomModel;

/**
 * ルームリポジトリ
 */
public interface RoomRepository {

    /**
     * IDからルームを取得
     * 
     * @param id ルームID
     * @return ルーム
     */
    Optional<RoomModel> selectById(final String id);

    /**
     * ルームを作成
     * 
     * @param roomModel ルーム
     */
    void insert(final RoomModel roomModel);

    /**
     * IDからルームを削除
     * 
     * @param id ルームID
     */
    void deleteById(final String id);

}
