package dev.abelab.smartpointer.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.abelab.smartpointer.domain.model.RoomModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.infrastructure.db.mapper.RoomMapper;
import dev.abelab.smartpointer.infrastructure.factory.RoomFactory;
import lombok.RequiredArgsConstructor;

/**
 * ルームリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomMapper roomMapper;

    private final RoomFactory roomFactory;

    @Override
    public Optional<RoomModel> selectById(final String id) {
        return Optional.ofNullable(this.roomMapper.selectByPrimaryKey(id)).map(RoomModel::new);
    }

    @Override
    public void insert(final RoomModel roomModel) {
        final var room = this.roomFactory.createRoom(roomModel);
        this.roomMapper.insert(room);
    }

    @Override
    public void deleteById(final String id) {
        this.roomMapper.deleteByPrimaryKey(id);
    }

}
