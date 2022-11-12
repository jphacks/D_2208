package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.type.Room;
import dev.abelab.smartpointer.usecase.room.CreateRoomUseCase;
import dev.abelab.smartpointer.usecase.room.DeleteRoomUseCase;
import lombok.RequiredArgsConstructor;

/**
 * ルームコントローラ
 */
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomUseCase createRoomUseCase;

    private final DeleteRoomUseCase deleteRoomUseCase;

    /**
     * ルーム作成API
     * 
     * @param pointerType ポインタータイプ
     * @return ルーム
     */
    @MutationMapping
    public Room createRoom( //
        @Argument final String pointerType //
    ) {
        return new Room(this.createRoomUseCase.handle(pointerType));
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

}
