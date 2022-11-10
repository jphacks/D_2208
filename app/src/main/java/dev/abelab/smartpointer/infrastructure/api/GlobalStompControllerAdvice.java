package dev.abelab.smartpointer.infrastructure.api;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import dev.abelab.smartpointer.exception.BaseException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.InternalServerErrorException;
import dev.abelab.smartpointer.infrastructure.api.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Global rest controller advice
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalStompControllerAdvice {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final MessageSource messageSource;

    /**
     * Handle exception
     * 
     * @param exception exception
     */
    @MessageExceptionHandler
    public void handleException(final Exception exception) {
        log.error(exception.getMessage(), exception);
        this.broadcastException(new InternalServerErrorException(ErrorCode.UNEXPECTED_ERROR));
    }

    /**
     * Handle base exception
     * 
     * @param exception exception
     */
    @MessageExceptionHandler
    public void handleException(final BaseException exception) {
        this.broadcastException(exception);
    }

    /**
     * 例外を配信
     * 
     * @param exception exception
     */
    private void broadcastException(final BaseException exception) {
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

        this.simpMessagingTemplate.convertAndSend("/topic/exception", body);
    }

}
