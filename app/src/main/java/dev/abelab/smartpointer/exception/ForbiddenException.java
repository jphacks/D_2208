package dev.abelab.smartpointer.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

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
        super(FORBIDDEN, errorCode);
    }

}
