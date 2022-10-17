package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

/**
 * タイマーコントローラ
 */
@Controller
@RequiredArgsConstructor
public class TimerController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * タイマー開始API
     */
    @MessageMapping("/rooms/{room_id}/timer/start")
    public void startTimer(@DestinationVariable final String roomId) {
        // TODO: TimerController::startTimerを実装
        // SimpMessagingTemplateはUseCaseから呼び出す
        this.simpMessagingTemplate.convertAndSend("/rooms/{room_id}/timer", "");
    }

    /**
     * タイマー停止API
     */
    @MessageMapping("/rooms/{room_id}/timer/stop")
    public void stopTimer(@DestinationVariable final String roomId) {
        // TODO: TimerController::startTimerを実装
    }

}
