package dev.abelab.smartpointer.usecase

import dev.abelab.smartpointer.domain.model.RoomModel
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.exception.NotFoundException
import dev.abelab.smartpointer.exception.UnauthorizedException
import dev.abelab.smartpointer.helper.RandomHelper
import dev.abelab.smartpointer.infrastructure.api.request.LoginRequest
import org.springframework.beans.factory.annotation.Autowired

/**
 * LoginUseCaseの単体テスト
 */
class LoginUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    LoginUseCase sut

    def "handle: ログインに成功するとアクセストークンを返す"() {
        given:
        final room = Spy(RoomModel)
        final requestBody = RandomHelper.mock(LoginRequest)

        when:
        this.sut.handle(requestBody)

        then:
        1 * this.roomRepository.selectById(requestBody.roomId) >> Optional.of(room)
        room.isTokenValid(requestBody.token) >> true
        1 * this.userService.checkIsNameAlreadyUsed(requestBody.roomId, requestBody.name) >> {}
        1 * this.userRepository.insert(_)
        noExceptionThrown()
    }

    def "handle: ルームが存在しない場合は404エラー"() {
        given:
        final requestBody = RandomHelper.mock(LoginRequest)

        when:
        this.sut.handle(requestBody)

        then:
        1 * this.roomRepository.selectById(requestBody.roomId) >> Optional.empty()
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_ROOM))
    }

    def "handle: トークンが間違えている場合は401エラー"() {
        given:
        final room = Spy(RoomModel)
        final requestBody = RandomHelper.mock(LoginRequest)

        when:
        this.sut.handle(requestBody)

        then:
        1 * this.roomRepository.selectById(requestBody.roomId) >> Optional.of(room)
        room.isTokenValid(requestBody.token) >> false
        final BaseException exception = thrown()
        verifyException(exception, new UnauthorizedException(ErrorCode.INVALID_ROOM_TOKEN))
    }

}
