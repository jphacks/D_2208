package dev.abelab.smartpointer.infrastructure.db.mapper;

import dev.abelab.smartpointer.infrastructure.db.mapper.base.RoomBaseMapper;

public interface RoomMapper extends RoomBaseMapper {

    void updatePointerTypeById(final String id, final String pointerType);

}
