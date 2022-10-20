package dev.abelab.smartpointer.domain.repository;

import java.util.Optional;

import dev.abelab.smartpointer.domain.model.TimerModel;

/**
 * タイマーリポジトリ
 */
public interface TimerRepository {

    /**
     * ルームIDからタイマーを取得
     * 
     * @param roomId ルームID
     * @return タイマー
     */
    Optional<TimerModel> selectByRoomId(final String roomId);

    /**
     * タイマーを作成
     * 
     * @param timerModel タイマー
     */
    void insert(final TimerModel timerModel);

    /**
     * タイマーをupsert
     * 
     * @param timerModel タイマー
     */
    void upsert(final TimerModel timerModel);

}
