package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.usecase.GoNextSlideUseCase;
import lombok.RequiredArgsConstructor;

/**
 * スライドコントローラ
 */
@Controller
@RequiredArgsConstructor
public class SlideStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final GoNextSlideUseCase goNextSlideUseCase;

    /**
     * スライドを進めるトピック
     * 
     * @param roomId ルームID
     */
    @MessageMapping("/rooms/{roomId}/slides/next")
    public void goNextSlide( //
        @DestinationVariable final String roomId //
    ) {
        this.simpMessagingTemplate.convertAndSend( //
            String.format("/topic/rooms/%s/slides/control", roomId), //
            this.goNextSlideUseCase.handle(roomId) //
        );
    }

}
