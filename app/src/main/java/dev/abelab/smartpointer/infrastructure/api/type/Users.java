package dev.abelab.smartpointer.infrastructure.api.type;

import java.util.List;
import java.util.stream.Collectors;

import dev.abelab.smartpointer.domain.model.RoomUsersModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザリスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    /**
     * ユーザリスト
     */
    List<User> users;

    public Users(final RoomUsersModel roomUsersModel) {
        this.users = roomUsersModel.getUsers().stream() //
            .map(User::new) //
            .collect(Collectors.toList());
    }

}
