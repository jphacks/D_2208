package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.auth.LoginUserDetails;
import dev.abelab.smartpointer.domain.model.PointerControlModel;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.infrastructure.api.type.PointerControl;
import dev.abelab.smartpointer.usecase.pointer.MovePointerUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * ポインターコントローラ
 */
@Controller
@RequiredArgsConstructor
public class PointerController {

    private final Flux<PointerControlModel> pointerControlFlux;

    private final Sinks.Many<PointerControlModel> pointerControlSink;

    private final MovePointerUseCase movePointerUseCase;

    /**
     * ポインター操作API
     *
     * @param loginUser ログインユーザ
     * @return ポインター操作
     */
    @MutationMapping
    public PointerControl movePointer( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @Argument final Double alpha, //
        @Argument final Double beta, //
        @Argument final Double gamma //
    ) {
        if (Objects.isNull(loginUser)) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN);
        }

        final var pointerControl = this.movePointerUseCase.handle(loginUser.getRoomId(), loginUser, alpha, beta, gamma);
        this.pointerControlSink.tryEmitNext(pointerControl);
        return new PointerControl(pointerControl);
    }

}
