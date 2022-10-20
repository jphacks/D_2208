package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.request.TimerResumeRequest;
import dev.abelab.smartpointer.infrastructure.api.request.TimerStartRequest;
import dev.abelab.smartpointer.infrastructure.api.validation.RequestValidated;
import dev.abelab.smartpointer.usecase.BroadcastTimerUseCase;
import dev.abelab.smartpointer.usecase.ResumeTimerUseCase;
import dev.abelab.smartpointer.usecase.StartTimerUseCase;
import dev.abelab.smartpointer.usecase.StopTimerUseCase;
import lombok.RequiredArgsConstructor;

/**
 * タイマーコントローラ
 */
@Controller
@RequiredArgsConstructor
public class TimerStompController {

    private final BroadcastTimerUseCase broadcastTimerUseCase;

    private final StartTimerUseCase startTimerUseCase;

    private final ResumeTimerUseCase resumeTimerUseCase;

    private final StopTimerUseCase stopTimerUseCase;

    /**
     * タイマー開始トピック
     * 
     * @param roomId ルームID
     * @param requestBody タイマー開始リクエスト
     * @param headers headers
     */
    @MessageMapping("/rooms/{room_id}/timer/start")
    public void startTimer( //
        @DestinationVariable("room_id") final String roomId, //
        @RequestValidated @Payload final TimerStartRequest requestBody, //
        final SimpMessageHeaderAccessor headers //
    ) {
        // TODO: ユーザ認証
        // final var authorization = headers.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        this.startTimerUseCase.handle(roomId, requestBody);
        this.broadcastTimerUseCase.handle(roomId);
    }

    /**
     * タイマー再開トピック
     * 
     * @param roomId ルームID
     * @param requestBody タイマー再開リクエスト
     */
    @MessageMapping("/rooms/{room_id}/timer/resume")
    public void resumeTimer( //
        @DestinationVariable final String roomId, //
        @RequestValidated @Payload final TimerResumeRequest requestBody //
    ) {
        this.resumeTimerUseCase.handle(roomId, requestBody);
        this.broadcastTimerUseCase.handle(roomId);
    }

    /**
     * タイマー停止トピック
     *
     * @param roomId ルームID
     */
    @MessageMapping("/rooms/{room_id}/timer/stop")
    public void stopTimer( //
        @DestinationVariable final String roomId //
    ) {
        this.stopTimerUseCase.handle(roomId);
        this.broadcastTimerUseCase.handle(roomId);
    }

    /**
     * タイマーリセットトピック
     *
     * @param roomId ルームID
     */
    @MessageMapping("/rooms/{room_id}/timer/reset")
    public void resetTimer( //
        @DestinationVariable final String roomId //
    ) {
        // TODO: TimerController::resetTimerを実装
    }

}
