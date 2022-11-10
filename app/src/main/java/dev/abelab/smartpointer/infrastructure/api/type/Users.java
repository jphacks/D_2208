package dev.abelab.smartpointer.infrastructure.api.type;

import java.util.List;

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

}
