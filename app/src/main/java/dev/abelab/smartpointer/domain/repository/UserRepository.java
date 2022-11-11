package dev.abelab.smartpointer.domain.repository;

import java.util.List;
import java.util.Optional;

import dev.abelab.smartpointer.domain.model.UserModel;

/**
 * ユーザリポジトリ
 */
public interface UserRepository {

    /**
     * IDからユーザを取得
     * 
     * @param id ユーザID
     * @return ユーザ
     */
    Optional<UserModel> selectById(final String id);

    /**
     * ユーザリストを取得
     * 
     * @return ユーザリスト
     */
    List<UserModel> selectAll();

    /**
     * ルームIDからユーザリストを取得
     *
     * @param roomId ルームID
     * @return ユーザリスト
     */
    List<UserModel> selectByRoomId(final String roomId);

    /**
     * ユーザを作成
     *
     * @param userModel ユーザ
     */
    void insert(final UserModel userModel);

    /**
     * IDからユーザを削除
     * 
     * @param id ユーザID
     */
    void deleteById(final String id);

    /**
     * ルームID、ユーザ名からユーザの存在チェック
     * 
     * @param roomId ルームID
     * @param name ユーザ名
     * @return 存在するか
     */
    boolean existsByRoomIdAndName(final String roomId, final String name);

}
