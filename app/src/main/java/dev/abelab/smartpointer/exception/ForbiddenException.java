package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.graphql.execution.ErrorType;

/**
 * 403 Forbidden
 */
public class ForbiddenException extends BaseException {

    /**
     * create forbidden exception
     *
     * @param errorCode error code
     */
    public ForbiddenException(final ErrorCode errorCode) {
        super(FORBIDDEN, ErrorType.FORBIDDEN, errorCode);
    }

}
