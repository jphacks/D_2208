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

    /**
     * 401 Unauthorized: 2000~2999
     */
    USER_NOT_LOGGED_IN(2000, "exception.unauthorized.user_not_logged_in"),

    INVALID_ROOM_TOKEN(2001, "exception.unauthorized.invalid_room_token"),

    /**
     * 403 Forbidden: 3000~3999
     */
    USER_HAS_NO_PERMISSION(3000, "exception.forbidden.user_has_no_permission"),

    /**
     * 404 Not Found: 4000~4999
     */
    NOT_FOUND_API(4000, "exception.not_found.api"),

    NOT_FOUND_ROOM(4001, "exception.not_found.room"),

    /**
     * 409 Conflict: 5000~5999
     */
    USER_NAME_IS_ALREADY_EXISTS(5000, "exception.conflict.user_name_is_already_exists"),

    /**
     * 500 Internal Server Error: 6000~6999
     */
    UNEXPECTED_ERROR(6000, "exception.internal_server_error.unexpected_error");

    /**
     * エラーコード
     */
    private final Integer code;

    /**
     * resources/i18n/messages.ymlのキーに対応
     */
    private final String messageKey;

}
