package dev.abelab.smartpointer.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルームユーザリストモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomUsersModel {

    /**
     * ルームID
     */
    String roomId;

    /**
     * ユーザリスト
     */
    List<UserModel> users;

}
