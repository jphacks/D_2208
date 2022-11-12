package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.domain.model.RoomCustomPointersModel;
import dev.abelab.smartpointer.infrastructure.api.type.CustomPointer;
import dev.abelab.smartpointer.infrastructure.api.type.CustomPointers;
import dev.abelab.smartpointer.usecase.custom_pointer.CreateCustomPointersUseCase;
import dev.abelab.smartpointer.usecase.custom_pointer.DeleteCustomPointersUseCase;
import dev.abelab.smartpointer.usecase.custom_pointer.GetCustomPointersUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * カスタムポインターコントローラ
 */
@Controller
@RequiredArgsConstructor
public class CustomPointerController {

    private final Sinks.Many<RoomCustomPointersModel> roomCustomPointersSink;

    private final Flux<RoomCustomPointersModel> roomCustomPointersFlux;

    private final GetCustomPointersUseCase getCustomPointersUseCase;

    private final CreateCustomPointersUseCase createCustomPointersUseCase;

    private final DeleteCustomPointersUseCase deleteCustomPointersUseCase;

    /**
     * カスタムポインターリスト取得API
     *
     * @param roomId ルームID
     * @return カスタムポインターリスト
     */
    @QueryMapping
    public CustomPointers getCustomPointers( //
        @Argument final String roomId //
    ) {
        final var customPointers = this.getCustomPointersUseCase.handle(roomId).stream() //
            .map(CustomPointer::new) //
            .collect(Collectors.toList());
        return new CustomPointers(customPointers);
    }

    /**
     * カスタムポインター作成API
     *
     * @param roomId ルームID
     * @param id カスタムポインターID
     * @param label ラベル
     * @param content 画像コンテンツ(Base64)
     * @return カスタムポインターID
     */
    @MutationMapping
    public String createCustomPointer( //
        @Argument final String roomId, //
        @Argument final String id, //
        @Argument final String label, //
        @Argument final String content //
    ) {
        this.createCustomPointersUseCase.handle(roomId, id, label, content);

        final var customPointers = this.getCustomPointersUseCase.handle(roomId);
        this.roomCustomPointersSink.tryEmitNext(new RoomCustomPointersModel(roomId, customPointers));

        return id;
    }

    /**
     * カスタムポインター削除API
     *
     * @param id カスタムポインターID
     * @param roomId ルームID
     * @return カスタムポインターID
     */
    @MutationMapping
    public String deleteCustomPointer( //
        @Argument final String id, //
        @Argument final String roomId //
    ) {
        this.deleteCustomPointersUseCase.handle(id, roomId);

        final var customPointers = this.getCustomPointersUseCase.handle(roomId);
        this.roomCustomPointersSink.tryEmitNext(new RoomCustomPointersModel(roomId, customPointers));

        return id;
    }

    /**
     * カスタムポインターリスト購読API
     *
     * @param roomId ルームID
     * @return カスタムポインターリスト
     */
    @SubscriptionMapping
    public Publisher<CustomPointers> subscribeToCustomPointers( //
        @Argument final String roomId //
    ) {
        return this.roomCustomPointersFlux //
            .filter(roomCustomPointersModel -> roomCustomPointersModel.getRoomId().equals(roomId)) //
            .map(CustomPointers::new);
    }

}
