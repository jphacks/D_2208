package dev.abelab.smartpointer.infrastructure.api.controller;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.infrastructure.api.type.Timer;
import dev.abelab.smartpointer.usecase.timer.*;
import dev.abelab.smartpointer.util.AuthUtil;
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

    private final AuthUtil authUtil;

    private final GetTimerUseCase getTimerUseCase;

    private final StartTimerUseCase startTimerUseCase;

    private final ResumeTimerUseCase resumeTimerUseCase;

    private final PauseTimerUseCase pauseTimerUseCase;

    private final ResetTimerUseCase resetTimerUseCase;

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
     * @param inputTime 入力時間
     * @param accessToken アクセストークン
     * @return タイマー
     */
    @MutationMapping
    public Timer startTimer( //
        @Argument final Integer inputTime, //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var timer = this.startTimerUseCase.handle(loginUser.getRoomId(), inputTime);
        this.timerSink.tryEmitNext(timer);
        return new Timer(timer);
    }

    /**
     * タイマー再開API
     *
     * @param accessToken アクセストークン
     * @return タイマー
     */
    @MutationMapping
    public Timer resumeTimer( //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var timer = this.resumeTimerUseCase.handle(loginUser.getRoomId());
        this.timerSink.tryEmitNext(timer);
        return new Timer(timer);
    }

    /**
     * タイマー一時停止API
     *
     * @param accessToken アクセストークン
     * @return タイマー
     */
    @MutationMapping
    public Timer pauseTimer( //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var timer = this.pauseTimerUseCase.handle(loginUser.getRoomId());
        this.timerSink.tryEmitNext(timer);
        return new Timer(timer);
    }

    /**
     * タイマーリセットAPI
     *
     * @param accessToken アクセストークン
     * @return タイマー
     */
    @MutationMapping
    public Timer resetTimer( //
        @Argument final String accessToken //
    ) {
        final var loginUser = this.authUtil.getLoginUser(accessToken);

        final var timer = this.resetTimerUseCase.handle(loginUser.getRoomId());
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
