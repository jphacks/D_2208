package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse;
import dev.abelab.smartpointer.usecase.CreateRoomUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * ルームコントローラ
 */
@Tag(name = "Room", description = "ルーム")
@RestController
@RequestMapping(path = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class RoomRestController {

    private final CreateRoomUseCase createRoomUseCase;

    /**
     * ルーム作成API
     * 
     * @return ルーム
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom() {
        return new RoomResponse(this.createRoomUseCase.handle());
    }

}
