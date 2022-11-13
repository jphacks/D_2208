package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.graphql.execution.ErrorType;

/**
 * 500 Unauthorized
 */
public class UnauthorizedException extends BaseException {

    /**
     * create unauthorized exception
     *
     * @param errorCode error code
     */
    public UnauthorizedException(final ErrorCode errorCode) {
        super(UNAUTHORIZED, ErrorType.UNAUTHORIZED, errorCode);
    }

}
