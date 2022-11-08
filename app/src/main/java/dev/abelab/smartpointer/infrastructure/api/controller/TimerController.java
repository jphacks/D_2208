package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.type.Timer;
import dev.abelab.smartpointer.usecase.timer.GetTimerUseCase;
import lombok.RequiredArgsConstructor;

/**
 * タイマーコントローラ
 */
@Controller
@RequiredArgsConstructor
public class TimerController {

    private final GetTimerUseCase getTimerUseCase;

    /**
     * タイマー取得API
     * 
     * @return タイマー
     */
    @QueryMapping
    public Timer getTimer( //
        @Argument final String roomId //
    ) {
        return new Timer(this.getTimerUseCase.handle(roomId));
    }

}
