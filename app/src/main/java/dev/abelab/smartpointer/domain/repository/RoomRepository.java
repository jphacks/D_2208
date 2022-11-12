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

    /**
     * IDからルームの存在チェック
     * 
     * @param id ルームID
     * @return チェック結果
     */
    boolean existsById(final String id);

    /**
     * IDからポインタータイプを更新
     *
     * @param id ルームID
     * @param pointerType ポインタータイプ
     */
    void updatePointerTypeById(final String id, final String pointerType);

}
