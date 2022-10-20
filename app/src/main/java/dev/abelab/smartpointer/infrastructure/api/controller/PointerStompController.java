package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.request.PointerControlRequest;
import dev.abelab.smartpointer.infrastructure.api.validation.RequestValidated;
import dev.abelab.smartpointer.usecase.ControlPointerUseCase;
import lombok.RequiredArgsConstructor;

/**
 * ポインターコントローラ
 */
@Controller
@RequiredArgsConstructor
public class PointerStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ControlPointerUseCase controlPointerUseCase;

    /**
     * ポインター操作トピック
     * 
     * @param roomId ルームID
     * @param requestBody ポインター操作リクエスト
     */
    @MessageMapping("/rooms/{roomId}/pointer/control")
    public void controlPointer( //
        @DestinationVariable final String roomId, //
        @RequestValidated @Payload final PointerControlRequest requestBody //
    ) {
        this.simpMessagingTemplate.convertAndSend( //
            String.format("/topic/rooms/%s/pointer/control", roomId), //
            this.controlPointerUseCase.handle(roomId, requestBody) //
        );
    }

}
