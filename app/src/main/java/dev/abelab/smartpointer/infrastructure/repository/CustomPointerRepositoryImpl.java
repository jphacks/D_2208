package dev.abelab.smartpointer.infrastructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import dev.abelab.smartpointer.domain.model.CustomPointerModel;
import dev.abelab.smartpointer.domain.repository.CustomPointerRepository;
import dev.abelab.smartpointer.infrastructure.db.entity.CustomPointerExample;
import dev.abelab.smartpointer.infrastructure.db.mapper.CustomPointerMapper;
import lombok.RequiredArgsConstructor;

/**
 * カスタムポインターリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class CustomPointerRepositoryImpl implements CustomPointerRepository {

    private final CustomPointerMapper customPointerMapper;

    @Override
    public List<CustomPointerModel> selectByRoomId(final String roomId) {
        final var example = new CustomPointerExample();
        example.createCriteria().andRoomIdEqualTo(roomId);
        return this.customPointerMapper.selectByExample(example).stream() //
            .map(CustomPointerModel::new) //
            .collect(Collectors.toList());
    }

}
