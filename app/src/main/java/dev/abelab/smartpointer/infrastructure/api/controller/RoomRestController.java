package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import dev.abelab.smartpointer.infrastructure.api.response.RoomResponse;
import dev.abelab.smartpointer.usecase.room.CreateRoomUseCase;
import dev.abelab.smartpointer.usecase.room.DeleteRoomUseCase;
import lombok.RequiredArgsConstructor;

/**
 * ルームコントローラ
 */
@RestController
@RequestMapping(path = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class RoomRestController {

    private final CreateRoomUseCase createRoomUseCase;

    private final DeleteRoomUseCase deleteRoomUseCase;

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
     * ルーム削除API
     * 
     * @param roomId ルームID
     */
    @DeleteMapping("/{room_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRoom(@PathVariable("room_id") final String roomId //
    ) {
        this.deleteRoomUseCase.handle(roomId);
    }

}
