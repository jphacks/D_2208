package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.response.AccessTokenResponse;
import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse;
import dev.abelab.smartpointer.usecase.room.CreateRoomUseCase;
import dev.abelab.smartpointer.usecase.room.DeleteRoomUseCase;
import dev.abelab.smartpointer.usecase.user.JoinRoomUseCase;
import lombok.RequiredArgsConstructor;

/**
 * ルームコントローラ
 */
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;

    private final DeleteRoomUseCase deleteRoomUseCase;

    private final JoinRoomUseCase joinRoomUseCase;

    /**
     * ルーム作成API
     * 
     * @return ルーム
     */
    @MutationMapping
    public RoomResponse createRoom() {
        return new RoomResponse(this.createRoomUseCase.handle());
    }

    /**
     * ルーム削除API
     *
     * @param roomId ルームID
     * @return ルームID
     */
    @MutationMapping
    public String deleteRoom( //
        @Argument final String roomId //
    ) {
        this.deleteRoomUseCase.handle(roomId);
        return roomId;
    }

    /**
     * ルーム入室API
     *
     * @param roomId ルームID
     * @return ルームID
     */
    @MutationMapping
    public AccessTokenResponse joinRoom( //
        @Argument final String roomId, //
        @Argument final String passcode, //
        @Argument final String userName //
    ) {
        return this.joinRoomUseCase.handle(roomId, passcode, userName);
    }

}
