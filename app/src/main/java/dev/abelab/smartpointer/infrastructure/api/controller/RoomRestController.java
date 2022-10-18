package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import dev.abelab.smartpointer.infrastructure.api.request.RoomJoinRequest;
import dev.abelab.smartpointer.infrastructure.api.response.AccessTokenResponse;
import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse;
import dev.abelab.smartpointer.infrastructure.api.validation.RequestValidated;
import dev.abelab.smartpointer.usecase.CreateRoomUseCase;
import dev.abelab.smartpointer.usecase.JoinRoomUseCase;
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

    private final JoinRoomUseCase joinRoomUseCase;

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

    /**
     * ルーム入室API
     *
     * @param roomId ルームID
     * @param requestBody ルーム入室リクエスト
     * @return アクセストークン
     */
    @PostMapping("/{room_id}/join")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse joinRoom( //
        @PathVariable("room_id") final String roomId, //
        @RequestValidated @RequestBody final RoomJoinRequest requestBody //
    ) {
        return this.joinRoomUseCase.handle(roomId, requestBody);
    }

}
