package dev.abelab.smartpointer.infrastructure.db.mapper;

import dev.abelab.smartpointer.infrastructure.db.entity.Timer;
import dev.abelab.smartpointer.infrastructure.db.mapper.base.TimerBaseMapper;

public interface TimerMapper extends TimerBaseMapper {

    void upsert(final Timer timer);

}
