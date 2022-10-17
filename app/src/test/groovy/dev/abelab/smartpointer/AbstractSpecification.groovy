package dev.abelab.smartpointer

import dev.abelab.smartpointer.exception.BaseException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Specification

/**
 * テストの基底クラス
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractSpecification extends Specification {

    @LocalServerPort
    protected Integer PORT

    /**
     * 例外を検証
     *
     * @param thrownException 発生した例外
     * @param expectedException 期待する例外
     */
    static void verifyException(final BaseException thrownException, final BaseException expectedException) {
        assert thrownException.httpStatus == expectedException.httpStatus
        assert thrownException.errorCode == expectedException.errorCode
    }

}
