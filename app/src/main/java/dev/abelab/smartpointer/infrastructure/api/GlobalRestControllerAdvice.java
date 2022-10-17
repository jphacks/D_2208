package dev.abelab.smartpointer.infrastructure.api;

import java.util.Locale;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.abelab.smartpointer.exception.*;
import dev.abelab.smartpointer.infrastructure.api.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Global rest controller advice
 */
@Slf4j
@Hidden
@Controller
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalRestControllerAdvice extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    /**
     * Handle not found exception
     *
     * @return response entity
     */
    @RequestMapping("/api/**")
    public ResponseEntity<Object> handleApiNotFoundException() {
        return this.buildResponseEntity(new NotFoundException(ErrorCode.NOT_FOUND_API));
    }

    /**
     * Handle unexpected exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        return this.buildResponseEntity(new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
    }

    /**
     * Handle base exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(final BaseException exception) {
        return this.buildResponseEntity(exception);
    }

    /**
     * Handle constraint violation exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException exception) {
        return this.buildResponseEntity(new BadRequestException(ErrorCode.VALIDATION_ERROR));
    }

    /**
     * Handle method argument type mismatch exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return this.buildResponseEntity(new BadRequestException(ErrorCode.INVALID_REQUEST_PARAMETER));
    }

    /**
     * Handle validation exception
     *
     * @param exception exception
     * @return response entity
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(final ValidationException exception) {
        final var causedException = exception.getCause();
        final var errorCode = causedException instanceof BaseException //
            ? ((BaseException) causedException).getErrorCode() //
            : ErrorCode.INVALID_REQUEST_PARAMETER;

        return this.buildResponseEntity(new BadRequestException(errorCode));
    }

    /**
     * Handle username not found exception
     * 
     * @param exception exception
     * @return then response entity
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(final UsernameNotFoundException exception) {
        return this.buildResponseEntity(new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN));
    }

    /**
     * Handle method argument not valid exception
     *
     * @param exception exception
     * @param headers headers
     * @param status status
     * @param request request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nullable final MethodArgumentNotValidException exception,
        @Nullable final HttpHeaders headers, @Nullable final HttpStatus status, @Nullable final WebRequest request) {
        return this.buildResponseEntity(new BadRequestException(ErrorCode.INVALID_REQUEST_PARAMETER));
    }

    /**
     * Handle http message not readable exception
     *
     * @param exception exception
     * @param headers headers
     * @param status status
     * @param request request
     * @return the response entity
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@Nullable final HttpMessageNotReadableException exception,
        @Nullable final HttpHeaders headers, @Nullable final HttpStatus status, @Nullable final WebRequest request) {
        return this.buildResponseEntity(new BadRequestException(ErrorCode.INVALID_REQUEST_PARAMETER));
    }

    /**
     * 例外からエラーレスポンスを作成
     * 
     * @param exception 例外
     * @return エラーレスポンス
     */
    private ResponseEntity<Object> buildResponseEntity(final BaseException exception) {
        final var message = this.messageSource.getMessage(exception.getErrorCode().getMessageKey(), null, Locale.ENGLISH);
        final var body = ErrorResponse.builder() //
            .code(exception.getErrorCode().getCode()) //
            .message(message) //
            .build();

        if (exception.getHttpStatus().is4xxClientError()) {
            log.warn(String.format("%d: %s", exception.getErrorCode().getCode(), message));
        } else if (exception.getHttpStatus().is5xxServerError()) {
            log.error(String.format("%d: %s", exception.getErrorCode().getCode(), message));
        }

        return new ResponseEntity<>(body, exception.getHttpStatus());
    }

}
