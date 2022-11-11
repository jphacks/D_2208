package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.Objects;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.auth.LoginUserDetails;
import dev.abelab.smartpointer.domain.model.SlideControlModel;
import dev.abelab.smartpointer.enums.SlideControl;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.usecase.slide.GoNextSlideUseCase;
import dev.abelab.smartpointer.usecase.slide.GoPreviousSlideUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * スライドコントローラ
 */
@Controller
@RequiredArgsConstructor
public class SlideController {

    private final Flux<SlideControlModel> slideControlFlux;

    private final Sinks.Many<SlideControlModel> slideControlSink;

    private final GoNextSlideUseCase goNextSlideUseCase;

    private final GoPreviousSlideUseCase goPreviousSlideUseCase;

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
        return slideControl.getSlideControl();
    }

    /**
     * スライドを戻すAPI
     *
     * @param loginUser ログインユーザ
     * @return スライド操作
     */
    @MutationMapping
    public SlideControl goPreviousSlide( //
        @AuthenticationPrincipal final LoginUserDetails loginUser //
    ) {
        if (Objects.isNull(loginUser)) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN);
        }

        final var slideControl = this.goPreviousSlideUseCase.handle(loginUser.getRoomId());
        this.slideControlSink.tryEmitNext(slideControl);
        return slideControl.getSlideControl();
    }

    /**
     * スライド操作購読API
     *
     * @param roomId ルームID
     * @return スライド操作
     */
    @SubscriptionMapping
    public Publisher<SlideControl> subscribeToSlideControl( //
        @Argument final String roomId //
    ) {
        return this.slideControlFlux //
            .filter(slideControlModel -> slideControlModel.getRoomId().equals(roomId)) //
            .map(SlideControlModel::getSlideControl);
    }

}
