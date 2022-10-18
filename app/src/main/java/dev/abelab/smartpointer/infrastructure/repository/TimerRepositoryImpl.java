package dev.abelab.smartpointer.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.domain.repository.TimerRepository;
import dev.abelab.smartpointer.infrastructure.db.mapper.TimerMapper;
import dev.abelab.smartpointer.infrastructure.factory.TimerFactory;
import lombok.RequiredArgsConstructor;

/**
 * タイマーリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class TimerRepositoryImpl implements TimerRepository {

    private final TimerMapper timerMapper;

    private final TimerFactory timerFactory;

    @Override
    public Optional<TimerModel> selectByRoomId(final String roomId) {
        return Optional.ofNullable(this.timerMapper.selectByPrimaryKey(roomId)).map(TimerModel::new);
    }

    @Override
    public void insert(final TimerModel timerModel) {
        final var timer = this.timerFactory.createTimer(timerModel);
        this.timerMapper.insert(timer);
    }

    @Override
    public void upsert(final TimerModel timerModel) {
        final var timer = this.timerFactory.createTimer(timerModel);
        this.timerMapper.upsert(timer);
    }

}
