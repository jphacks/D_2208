package dev.abelab.smartpointer.exception;

import org.springframework.http.HttpStatus;

/**
 * 例外の基底クラス
 */
public class BaseException extends RuntimeException {

    /**
     * http status
     */
    private final HttpStatus httpStatus;

    /**
     * error code
     */
    private final ErrorCode errorCode;

    /**
     * args
     */
    private final String[] args;

    /**
     * create base exception
     *
     * @param httpStatus http status
     * @param errorCode error code
     */
    public BaseException(final HttpStatus httpStatus, final ErrorCode errorCode, final String... args) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * getter of http status
     */
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    /**
     * getter of error code
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    /**
     * getter of args
     */
    public String[] getArgs() {
        return this.args;
    }

}
