package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.usecase.GoNextSlideUseCase;
import dev.abelab.smartpointer.usecase.GoPreviousSlideUseCase;
import lombok.RequiredArgsConstructor;

/**
 * スライドコントローラ
 */
@Controller
@RequiredArgsConstructor
public class SlideStompController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final GoNextSlideUseCase goNextSlideUseCase;

    private final GoPreviousSlideUseCase goPreviousSlideUseCase;

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

    /**
     * スライドを戻すトピック
     *
     * @param roomId ルームID
     */
    @MessageMapping("/rooms/{roomId}/slides/previous")
    public void goPreviousSlide( //
        @DestinationVariable final String roomId //
    ) {
        this.simpMessagingTemplate.convertAndSend( //
            String.format("/topic/rooms/%s/slides/control", roomId), //
            this.goPreviousSlideUseCase.handle(roomId) //
        );
    }

}
