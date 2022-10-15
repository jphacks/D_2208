package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 400 Bad Request
 */
public class BadRequestException extends BaseException {

    /**
     * create bad request exception
     *
     * @param errorCode error code
     */
    public BadRequestException(final ErrorCode errorCode) {
        super(BAD_REQUEST, errorCode);
    }

}
