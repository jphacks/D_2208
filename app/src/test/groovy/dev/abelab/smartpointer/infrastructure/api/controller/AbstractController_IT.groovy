package dev.abelab.smartpointer.infrastructure.api.controller

import dev.abelab.smartpointer.AbstractDatabaseSpecification
import dev.abelab.smartpointer.exception.BaseException
import dev.abelab.smartpointer.property.AuthProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.context.MessageSource
import org.springframework.graphql.test.tester.WebGraphQlTester

/**
 * Controller統合テストの基底クラス
 */
@AutoConfigureHttpGraphQlTester
abstract class AbstractController_IT extends AbstractDatabaseSpecification {

    @Autowired
    private WebGraphQlTester graphQlTester

    @Autowired
    private MessageSource messageSource

    @Autowired
    protected AuthProperty authProperty

    /**
     * Execute query / return response
     *
     * @param query query
     * @param operation operation
     * @param clazz clazz
     * @return response
     */
    def <T> T execute(final String query, final String operation, final Class<T> clazz) {
        final response = this.graphQlTester.document(query).execute()
            .path(operation)
            .entity(clazz)

        return response.get()
    }

    /**
     * Execute query / verify exception
     *
     * @param query query
     * @param exception expected exception
     */
    def execute(final String query, final BaseException exception) {
        final expectedErrorMessage = this.getErrorMessage(exception)
        this.graphQlTester.document(query).execute()
            .errors()
            .satisfy({
                assert it[0].errorType == exception.errorType
                assert it[0].message == expectedErrorMessage
            })
    }

    /**
     * エラーメッセージを取得
     *
     * @param exception exception
     * @return エラーメッセージ
     */
    private String getErrorMessage(final BaseException exception) {
        final messageKey = exception.errorCode.messageKey
        final args = exception.args
        return this.messageSource.getMessage(messageKey, args, Locale.ENGLISH)
    }

}
