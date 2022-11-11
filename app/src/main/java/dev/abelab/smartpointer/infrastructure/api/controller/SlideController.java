package dev.abelab.smartpointer.infrastructure.api.controller;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.domain.model.SlideControlModel;
import dev.abelab.smartpointer.enums.SlideControl;
import dev.abelab.smartpointer.usecase.slide.GoNextSlideUseCase;
import dev.abelab.smartpointer.usecase.slide.GoPreviousSlideUseCase;
import dev.abelab.smartpointer.util.AuthUtil;
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

    private final AuthUtil authUtil;

    private final GoNextSlideUseCase goNextSlideUseCase;

    private final GoPreviousSlideUseCase goPreviousSlideUseCase;

    /**
     * スライドを進めるAPI
     *
     * @param accessToken アクセストークン
     * @return スライド操作
     */
    @MutationMapping
    public SlideControl goNextSlide( //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var slideControl = this.goNextSlideUseCase.handle(loginUser.getRoomId());
        this.slideControlSink.tryEmitNext(slideControl);
        return slideControl.getSlideControl();
    }

    /**
     * スライドを戻すAPI
     *
     * @param accessToken アクセストークン
     * @return スライド操作
     */
    @MutationMapping
    public SlideControl goPreviousSlide( //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

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
