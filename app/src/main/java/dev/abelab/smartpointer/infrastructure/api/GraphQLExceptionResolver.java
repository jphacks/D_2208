package dev.abelab.smartpointer.infrastructure.api;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.exception.BaseException;
import dev.abelab.smartpointer.infrastructure.api.response.ErrorResponse;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * GraphQL Exception Resolver
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private final MessageSource messageSource;

    @Override
    protected GraphQLError resolveToSingleError(final Throwable ex, final DataFetchingEnvironment env) {
        if (ex instanceof BaseException) {
            final var exception = (BaseException) ex;
            final var errorResponse = this.buildResponseEntity(exception);
            return GraphqlErrorBuilder.newError() //
                .errorType(exception.getErrorType()) //
                .message(errorResponse.getMessage()) //
                .path(env.getExecutionStepInfo().getPath()) //
                .location(env.getField().getSourceLocation()) //
                .build();
        } else {
            return null;
        }
    }

    /**
     * 例外からエラーレスポンスを作成
     *
     * @param exception 例外
     * @return エラーレスポンス
     */
    private ErrorResponse buildResponseEntity(final BaseException exception) {
        final var message = this.messageSource.getMessage(exception.getErrorCode().getMessageKey(), null, Locale.ENGLISH);
        final var response = ErrorResponse.builder() //
            .code(exception.getErrorCode().getCode()) //
            .message(message) //
            .build();

        if (exception.getHttpStatus().is4xxClientError()) {
            log.warn(String.format("%d: %s", exception.getErrorCode().getCode(), message));
        } else if (exception.getHttpStatus().is5xxServerError()) {
            log.error(String.format("%d: %s", exception.getErrorCode().getCode(), message));
        }

        return response;
    }

}
