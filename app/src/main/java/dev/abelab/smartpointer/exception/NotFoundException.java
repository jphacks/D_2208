package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        super(NOT_FOUND, errorCode);
    }

}
