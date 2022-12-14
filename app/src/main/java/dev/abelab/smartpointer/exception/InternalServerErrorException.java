package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.graphql.execution.ErrorType;

/**
 * 500 Internal Server Error
 */
public class InternalServerErrorException extends BaseException {

    /**
     * create conflict exception
     *
     * @param errorCode error code
     */
    public InternalServerErrorException(final ErrorCode errorCode) {
        super(INTERNAL_SERVER_ERROR, ErrorType.INTERNAL_ERROR, errorCode);
    }

}
