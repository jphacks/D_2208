package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.domain.model.RoomUsersEventModel;
import dev.abelab.smartpointer.infrastructure.api.type.AccessToken;
import dev.abelab.smartpointer.infrastructure.api.type.User;
import dev.abelab.smartpointer.infrastructure.api.type.Users;
import dev.abelab.smartpointer.usecase.user.GetUsersUseCase;
import dev.abelab.smartpointer.usecase.user.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * ユーザコントローラ
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final Sinks.Many<RoomUsersEventModel> roomUsersEventSink;

    private final Flux<RoomUsersEventModel> roomUsersEventFlux;

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
        final var accessToken = this.joinRoomUseCase.handle(roomId, passcode, userName);
        final var users = this.getUsersUseCase.handle(roomId);
        this.roomUsersEventSink.tryEmitNext(new RoomUsersEventModel(roomId, users));

        return accessToken;
    }

    /**
     * ユーザリスト購読API
     *
     * @param roomId ルームID
     * @return ユーザリスト
     */
    @SubscriptionMapping
    public Publisher<Users> subscribeToUsers( //
        @Argument final String roomId //
    ) {
        return this.roomUsersEventFlux //
            .filter(roomUsersEventModel -> roomUsersEventModel.getRoomId().equals(roomId)) //
            .map(Users::new);
    }

}
