package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.graphql.execution.ErrorType;

/**
 * 404 Not Found
 */
public class NotFoundException extends BaseException {

    /**
     * create not found exception
     *
     * @param errorCode error code
     */
    public NotFoundException(final ErrorCode errorCode) {
        super(NOT_FOUND, ErrorType.NOT_FOUND, errorCode);
    }

}
