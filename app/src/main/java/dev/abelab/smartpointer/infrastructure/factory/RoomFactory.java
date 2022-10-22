package dev.abelab.smartpointer.infrastructure.factory;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.RoomModel;
import dev.abelab.smartpointer.infrastructure.db.entity.Room;

/**
 * ルームファクトリ
 */
@Component
public class RoomFactory {

    /**
     * Roomを作成
     *
     * @param roomModel model
     * @return entity
     */
    public Room createRoom(final RoomModel roomModel) {
        return Room.builder() //
            .id(roomModel.getId()) //
            .passcode(roomModel.getPasscode()) //
            .build();
    }

}
