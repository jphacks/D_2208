package dev.abelab.smartpointer.exception;

import org.springframework.graphql.execution.ErrorType;
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
     * GraphQL error type
     */
    private final ErrorType errorType;

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
    public BaseException(final HttpStatus httpStatus, final ErrorType errorType, final ErrorCode errorCode, final String... args) {
        this.httpStatus = httpStatus;
        this.errorType = errorType;
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
     * getter of http status
     */
    public ErrorType getErrorType() {
        return this.errorType;
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
