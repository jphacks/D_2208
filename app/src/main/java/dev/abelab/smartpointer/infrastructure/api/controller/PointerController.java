package dev.abelab.smartpointer.infrastructure.api.controller;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.domain.model.PointerControlModel;
import dev.abelab.smartpointer.domain.model.RoomPointerModel;
import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.infrastructure.api.type.PointerControl;
import dev.abelab.smartpointer.infrastructure.api.type.User;
import dev.abelab.smartpointer.usecase.pointer.ChangePointerTypeUseCase;
import dev.abelab.smartpointer.usecase.pointer.DisconnectPointerUseCase;
import dev.abelab.smartpointer.usecase.pointer.GetPointerTypeUseCase;
import dev.abelab.smartpointer.usecase.pointer.MovePointerUseCase;
import dev.abelab.smartpointer.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * ポインターコントローラ
 */
@Controller
@RequiredArgsConstructor
public class PointerController {

    private final Flux<RoomPointerModel> roomPointerFlux;

    private final Sinks.Many<RoomPointerModel> roomPointerSink;

    private final Flux<PointerControlModel> pointerControlFlux;

    private final Sinks.Many<PointerControlModel> pointerControlSink;

    private final Flux<UserModel> pointerDisconnectFlux;

    private final Sinks.Many<UserModel> pointerDisconnectSink;

    private final AuthUtil authUtil;

    private final MovePointerUseCase movePointerUseCase;

    private final DisconnectPointerUseCase disconnectPointerUseCase;

    private final GetPointerTypeUseCase getPointerTypeUseCase;

    private final ChangePointerTypeUseCase changePointerTypeUseCase;

    /**
     * ポインター操作API
     *
     * @param accessToken アクセストークン
     * @return ポインター操作
     */
    @MutationMapping
    public PointerControl movePointer( //
        @Argument final Double alpha, //
        @Argument final Double beta, //
        @Argument final Double gamma, //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var pointerControl = this.movePointerUseCase.handle(loginUser.getRoomId(), loginUser, alpha, beta, gamma);
        this.pointerControlSink.tryEmitNext(pointerControl);
        return new PointerControl(pointerControl);
    }

    /**
     * ポインター切断API
     *
     * @param accessToken アクセストークン
     * @return ユーザ
     */
    @MutationMapping
    public User disconnectPointer( //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var user = this.disconnectPointerUseCase.handle(loginUser.getRoomId(), loginUser);
        this.pointerDisconnectSink.tryEmitNext(user);
        return new User(user);
    }

    /**
     * ポインタータイプ取得API
     *
     * @param roomId ルームID
     * @return ポインタータイプ
     */
    @QueryMapping
    public String getPointerType( //
        @Argument final String roomId //
    ) {
        return this.getPointerTypeUseCase.handle(roomId);
    }

    /**
     * ポインタータイプ変更API
     *
     * @param pointerType ポインタータイプ
     * @param roomId ルームID
     * @return ポインタータイプ
     */
    @MutationMapping
    public String changePointerType( //
        @Argument final String pointerType, //
        @Argument final String roomId //
    ) {
        this.changePointerTypeUseCase.handle(pointerType, roomId);
        this.roomPointerSink.tryEmitNext(new RoomPointerModel(roomId, pointerType));

        return pointerType;
    }

    /**
     * ポインター操作購読API
     *
     * @param roomId ルームID
     * @return ポインター
     */
    @SubscriptionMapping
    public Publisher<PointerControl> subscribeToPointer( //
        @Argument final String roomId //
    ) {
        return this.pointerControlFlux //
            .filter(pointerControlModel -> pointerControlModel.getRoomId().equals(roomId)) //
            .map(PointerControl::new);
    }

    /**
     * ポインター切断イベント購読API
     *
     * @param roomId ルームID
     * @return ポインター
     */
    @SubscriptionMapping
    public Publisher<User> subscribeToPointerDisconnectEvent( //
        @Argument final String roomId //
    ) {
        return this.pointerDisconnectFlux //
            .filter(userModel -> userModel.getRoomId().equals(roomId)) //
            .map(User::new);
    }

    /**
     * ポインタータイプ購読API
     *
     * @param roomId ルームID
     * @return ポインタータイプ
     */
    @SubscriptionMapping
    public Publisher<String> subscribeToPointerType( //
        @Argument final String roomId //
    ) {
        return this.roomPointerFlux //
            .filter(pointerControlFlux -> pointerControlFlux.getRoomId().equals(roomId)) //
            .map(RoomPointerModel::getPointerType);
    }

}
