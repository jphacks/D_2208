package dev.abelab.smartpointer.usecase

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.property.AuthProperty
import org.springframework.beans.factory.annotation.Autowired

/**
 * JoinRoomUseCaseの単体テスト
 */
class JoinRoomUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    JoinRoomUseCase sut

    @Autowired
    AuthProperty authProperty;

    def "handle: ログインに成功するとアクセストークンを返す"() {
        given:
        final room = Spy(RoomModel)
        final passcode = RandomHelper.alphanumeric(6)
        final userName = RandomHelper.alphanumeric(10)

        when:
        final result = this.sut.handle(room.id, passcode, userName)

        then:
        1 * this.roomRepository.selectById(room.id) >> Optional.of(room)
        1 * room.isPasscodeValid(passcode) >> true
        1 * this.userService.checkIsNameValid(userName) >> {}
        1 * this.userService.checkIsNameAlreadyUsed(room.id, userName) >> {}
        1 * this.userRepository.insert(_)
        result.tokenType == this.authProperty.tokenType
        result.ttl == this.authProperty.ttl
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final room = Spy(RoomModel)
        final passcode = RandomHelper.alphanumeric(6)
        final userName = RandomHelper.alphanumeric(10)

        when:
        this.sut.handle(room.id, passcode, userName)

        then:
        1 * this.roomRepository.selectById(room.id) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "handle: パスコードが間違えている場合は401エラー"() {
        given:
        final room = Spy(RoomModel)
        final passcode = RandomHelper.alphanumeric(6)
        final userName = RandomHelper.alphanumeric(10)

        when:
        this.sut.handle(room.id, passcode, userName)

        then:
        1 * this.roomRepository.selectById(room.id) >> Optional.of(room)
        room.isPasscodeValid(passcode) >> false
        final BaseException exception = thrown()
        verifyException(exception, new UnauthorizedException(ErrorCode.INCORRECT_ROOM_PASSCODE))
    }

}
