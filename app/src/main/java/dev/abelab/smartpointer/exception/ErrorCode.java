package dev.abelab.smartpointer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * エラーコード
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 400 Bad Request: 1000~1999
     */
    VALIDATION_ERROR(1000, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(1001, "exception.bad_request.invalid_request_parameter"),

    INVALID_USER_NAME(1002, "exception.bad_request.invalid_user_name"),

    TIMER_CANNOT_BE_STARTED(1003, "exception.bad_request.timer_cannot_be_started"),

    TIMER_CANNOT_BE_PAUSED(1004, "exception.bad_request.timer_cannot_be_paused"),

    TIMER_CANNOT_BE_RESET(1005, "exception.bad_request.timer_cannot_be_reset"),

    TIMER_CANNOT_BE_RESUMED(1006, "exception.bad_request.timer_cannot_be_resumed"),

    INVALID_TIMER_INPUT_TIME(1007, "exception.bad_request.invalid_timer_input_time"),

    /**
     * 401 Unauthorized: 2000~2999
     */
    USER_NOT_LOGGED_IN(2000, "exception.unauthorized.user_not_logged_in"),

    INCORRECT_ROOM_PASSCODE(2001, "exception.unauthorized.incorrect_room_passcode"),

    INVALID_ACCESS_TOKEN(2002, "exception.unauthorized.invalid_access_token"),

    /**
     * 403 Forbidden: 3000~3999
     */
    USER_HAS_NO_PERMISSION(3000, "exception.forbidden.user_has_no_permission"),

    /**
     * 404 Not Found: 4000~4999
     */
    NOT_FOUND_API(4000, "exception.not_found.api"),

    NOT_FOUND_ROOM(4001, "exception.not_found.room"),

    NOT_FOUND_TIMER(4002, "exception.not_found.timer"),

    NOT_FOUND_CUSTOM_POINTER(4003, "exception.not_found.custom_pointer"),

    /**
     * 409 Conflict: 5000~5999
     */
    USER_NAME_IS_ALREADY_EXISTS(5000, "exception.conflict.user_name_is_already_exists"),

    CUSTOM_POINTER_IS_ALREADY_EXISTS(5001, "exception.conflict.custom_pointer_is_already_exists"),

    /**
     * 500 Internal Server Error: 6000~6999
     */
    UNEXPECTED_ERROR(6000, "exception.internal_server_error.unexpected_error"),

    FAILED_TO_UPLOAD_FILE(6001, "exception.internal_server_error.failed_to_upload_file");

    /**
     * エラーコード
     */
    private final Integer code;

    /**
     * resources/i18n/messages.ymlのキーに対応
     */
    private final String messageKey;

}
