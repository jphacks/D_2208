package dev.abelab.smartpointer.domain.service

import dev.abelab.smartpointer.exception.BadRequestException
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.exception.ConflictException
import dev.abelab.smartpointer.exception.ErrorCode
import dev.abelab.smartpointer.helper.RandomHelper
import org.springframework.beans.factory.annotation.Autowired

/**
 * UserServiceの単体テスト
 */
class UserService_UT extends AbstractService_UT {

    @Autowired
    UserService sut

    def "checkIsNameAlreadyUsed: 対象ルームに同じユーザ名が存在しない場合は何もしない"() {
        given:
        final roomId = RandomHelper.alphanumeric(10)
        final userName = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsNameAlreadyUsed(roomId, userName)

        then:
        1 * this.userRepository.existsByRoomIdAndName(roomId, userName) >> false
        noExceptionThrown()
    }

    def "checkIsNameAlreadyUsed: 対象ルームに同じユーザ名が存在する場合は409エラー"() {
        given:
        final roomId = RandomHelper.alphanumeric(10)
        final userName = RandomHelper.alphanumeric(10)

        when:
        this.sut.checkIsNameAlreadyUsed(roomId, userName)

        then:
        1 * this.userRepository.existsByRoomIdAndName(roomId, userName) >> true
        final BaseException exception = thrown()
        verifyException(exception, new ConflictException(ErrorCode.USER_NAME_IS_ALREADY_EXISTS))
    }

    def "checkIsNameValid: ユーザ名が有効な場合は何もしない"() {
        when:
        this.sut.checkIsNameValid(inputUserName)

        then:
        noExceptionThrown()

        where:
        inputUserName << [
            RandomHelper.alphanumeric(1),
            RandomHelper.alphanumeric(255),
        ]
    }

    def "checkIsNameValid: ユーザ名が不正な場合は400エラー"() {
        when:
        this.sut.checkIsNameValid(inputUserName)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new BadRequestException(ErrorCode.INVALID_USER_NAME))

        where:
        inputUserName << [
            RandomHelper.alphanumeric(0),
            RandomHelper.alphanumeric(256),
        ]
    }

}
