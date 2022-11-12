package dev.abelab.smartpointer.infrastructure.api.controller;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.domain.model.RoomFinishEventModel;
import dev.abelab.smartpointer.infrastructure.api.type.Room;
import dev.abelab.smartpointer.usecase.room.CreateRoomUseCase;
import dev.abelab.smartpointer.usecase.room.DeleteRoomUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * ルームコントローラ
 */
@Controller
@RequiredArgsConstructor
public class RoomController {

    private final Flux<RoomFinishEventModel> roomFinishEventFlux;

    private final Sinks.Many<RoomFinishEventModel> roomFinishEventSink;

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
        this.roomFinishEventSink.tryEmitNext(new RoomFinishEventModel(roomId));

        return roomId;
    }

    /**
     * ルーム終了イベント購読API
     *
     * @param roomId ルームID
     * @return ルームID
     */
    @SubscriptionMapping
    public Publisher<String> subscribeToRoomFinishEvent( //
        @Argument final String roomId //
    ) {
        return this.roomFinishEventFlux //
            .filter(roomFinishEventModel -> roomFinishEventModel.getRoomId().equals(roomId)) //
            .map(RoomFinishEventModel::getRoomId);
    }

}
