package dev.abelab.smartpointer.infrastructure.api.controller;

import java.security.Principal;

import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.request.TimerStartRequest;
import dev.abelab.smartpointer.infrastructure.api.validation.RequestValidated;
import dev.abelab.smartpointer.usecase.BroadcastTimerUseCase;
import dev.abelab.smartpointer.usecase.StartTimerUseCase;
import lombok.RequiredArgsConstructor;

/**
 * タイマーコントローラ
 */
@Controller
@RequiredArgsConstructor
public class TimerStompController {

    private final BroadcastTimerUseCase broadcastTimerUseCase;

    private final StartTimerUseCase startTimerUseCase;

    /**
     * タイマー開始トピック
     * 
     * @param roomId ルームID
     * @param requestBody タイマー開始リクエスト
     * @param headers headers
     * @param principal principal
     */
    @MessageMapping("/rooms/{room_id}/timer/start")
    public void startTimer( //
        @DestinationVariable("room_id") final String roomId, //
        @RequestValidated @Payload final TimerStartRequest requestBody, //
        final SimpMessageHeaderAccessor headers, //
        final Principal principal //
    ) {
        final var authorization = headers.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        // TODO: ユーザ認証(@Principal経由で取得できるっぽい？)
        this.startTimerUseCase.handle(roomId, requestBody);
        this.broadcastTimerUseCase.handle(roomId);
    }

    /**
     * タイマー再開トピック
     */
    @MessageMapping("/rooms/{room_id}/timer/resume")
    public void resumeTimer( //
        @DestinationVariable final String roomId //
    ) {
        // TODO: TimerController::resumeTimerを実装
    }

    /**
     * タイマー停止トピック
     */
    @MessageMapping("/rooms/{room_id}/timer/stop")
    public void stopTimer( //
        @DestinationVariable final String roomId //
    ) {
        // TODO: TimerController::startTimerを実装
    }

    /**
     * タイマーリセットトピック
     */
    @MessageMapping("/rooms/{room_id}/timer/reset")
    public void resetTimer( //
        @DestinationVariable final String roomId //
    ) {
        // TODO: TimerController::resetTimerを実装
    }

}
