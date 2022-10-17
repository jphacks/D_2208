package dev.abelab.smartpointer.infrastructure.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.domain.repository.UserRepository;
import dev.abelab.smartpointer.infrastructure.db.entity.UserExample;
import dev.abelab.smartpointer.infrastructure.db.mapper.UserMapper;
import dev.abelab.smartpointer.infrastructure.factory.UserFactory;
import lombok.RequiredArgsConstructor;

/**
 * ユーザリポジトリ
 */
@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    private final UserFactory userFactory;

    @Override
    public Optional<UserModel> selectById(final String id) {
        return Optional.ofNullable(this.userMapper.selectByPrimaryKey(id)).map(UserModel::new);
    }

    @Override
    public List<UserModel> selectByRoomId(final String roomId) {
        final var example = new UserExample();
        example.createCriteria().andRoomIdEqualTo(roomId);
        return this.userMapper.selectByExample(example).stream() //
            .map(UserModel::new) //
            .collect(Collectors.toList());
    }

    @Override
    public void insert(final UserModel userModel) {
        final var user = this.userFactory.createUser(userModel);
        this.userMapper.insert(user);
    }

    @Override
    public void deleteById(final String id) {
        this.userMapper.deleteByPrimaryKey(id);
    }

}
