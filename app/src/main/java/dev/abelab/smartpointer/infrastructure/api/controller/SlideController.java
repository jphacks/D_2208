package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.auth.LoginUserDetails;
import dev.abelab.smartpointer.enums.SlideControl;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.usecase.slide.GoNextSlideUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * スライドコントローラ
 */
@Controller
@RequiredArgsConstructor
public class SlideController {

    private final Flux<SlideControl> slideControlFlux;

    private final Sinks.Many<SlideControl> slideControlSink;

    private final GoNextSlideUseCase goNextSlideUseCase;

    /**
     * スライドを進めるAPI
     *
     * @param loginUser ログインユーザ
     * @return スライド操作
     */
    @MutationMapping
    public SlideControl goNextSlide( //
        @AuthenticationPrincipal final LoginUserDetails loginUser //
    ) {
        if (Objects.isNull(loginUser)) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN);
        }

        final var slideControl = this.goNextSlideUseCase.handle(loginUser.getRoomId());
        this.slideControlSink.tryEmitNext(slideControl);
        return slideControl;
    }

}
