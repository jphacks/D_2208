package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.type.AccessToken;
import dev.abelab.smartpointer.infrastructure.api.type.User;
import dev.abelab.smartpointer.infrastructure.api.type.Users;
import dev.abelab.smartpointer.usecase.user.GetUsersUseCase;
import dev.abelab.smartpointer.usecase.user.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;

/**
 * ユーザコントローラ
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final GetUsersUseCase getUsersUseCase;

    private final JoinRoomUseCase joinRoomUseCase;

    /**
     * ユーザリスト取得API
     * 
     * @param roomId ルームID
     * @return ユーザリスト
     */
    @QueryMapping
    public Users getUsers( //
        @Argument final String roomId //
    ) {
        final var users = this.getUsersUseCase.handle(roomId).stream() //
            .map(User::new) //
            .collect(Collectors.toList());
        return new Users(users);
    }

    /**
     * ルーム入室API
     *
     * @param roomId ルームID
     * @return ルームID
     */
    @MutationMapping
    public AccessToken joinRoom( //
        @Argument final String roomId, //
        @Argument final String passcode, //
        @Argument final String userName //
    ) {
        return this.joinRoomUseCase.handle(roomId, passcode, userName);
    }

}
