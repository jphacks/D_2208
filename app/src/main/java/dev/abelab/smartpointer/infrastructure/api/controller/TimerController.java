package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.Objects;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.auth.LoginUserDetails;
import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.infrastructure.api.type.Timer;
import dev.abelab.smartpointer.usecase.timer.GetTimerUseCase;
import dev.abelab.smartpointer.usecase.timer.StartTimerUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * タイマーコントローラ
 */
@Controller
@RequiredArgsConstructor
public class TimerController {

    private final Flux<TimerModel> timerFlux;

    private final Sinks.Many<TimerModel> timerSink;

    private final GetTimerUseCase getTimerUseCase;
    private final StartTimerUseCase startTimerUseCase;

    /**
     * タイマー取得API
     * 
     * @param roomId ルームID
     * @return タイマー
     */
    @QueryMapping
    public Timer getTimer( //
        @Argument final String roomId //
    ) {
        return new Timer(this.getTimerUseCase.handle(roomId));
    }

    /**
     * タイマー開始API
     * 
     * @param loginUser ログインユーザ
     * @param inputTime 入力時間
     * @return タイマー
     */
    @MutationMapping
    public Timer startTimer( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @Argument final Integer inputTime //
    ) {
        if (Objects.isNull(loginUser)) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN);
        }

        final var timer = this.startTimerUseCase.handle(loginUser.getRoomId(), inputTime);
        this.timerSink.tryEmitNext(timer);
        return new Timer(timer);
    }

    /**
     * タイマー購読API
     * 
     * @param roomId ルームID
     * @return タイマー
     */
    @SubscriptionMapping
    public Publisher<Timer> subscribeToTimer( //
        @Argument final String roomId //
    ) {
        return this.timerFlux //
            .filter(timerModel -> timerModel.getRoomId().equals(roomId)) //
            .map(Timer::new);
    }

}
