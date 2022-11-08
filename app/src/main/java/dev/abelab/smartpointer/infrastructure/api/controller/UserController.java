package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import dev.abelab.smartpointer.infrastructure.api.request.RoomJoinRequest;
import dev.abelab.smartpointer.infrastructure.api.response.AccessTokenResponse;
import dev.abelab.smartpointer.infrastructure.api.response.UserResponse;
import dev.abelab.smartpointer.infrastructure.api.response.UsersResponse;
import dev.abelab.smartpointer.infrastructure.api.validation.RequestValidated;
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
    public UsersResponse getUsers( //
        @Argument final String roomId //
    ) {
        final var users = this.getUsersUseCase.handle(roomId).stream() //
            .map(UserResponse::new) //
            .collect(Collectors.toList());
        return new UsersResponse(users);
    }

    /**
     * ルーム入室API
     *
     * @param roomId ルームID
     * @param requestBody ルーム入室リクエスト
     * @return アクセストークン
     */
    @PostMapping("/{room_id}/join")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse joinRoom( //
        @PathVariable("room_id") final String roomId, //
        @RequestValidated @RequestBody final RoomJoinRequest requestBody //
    ) {
        return this.joinRoomUseCase.handle(roomId, requestBody.getPasscode(), requestBody.getName());
    }

}
