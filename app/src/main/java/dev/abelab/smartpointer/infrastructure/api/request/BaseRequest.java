package dev.abelab.smartpointer.infrastructure.api.request;

import dev.abelab.smartpointer.exception.BaseException;

/**
 * リクエストボディのインターフェース
 */
public interface BaseRequest {

    /**
     * リクエストボディのバリデーション
     */
    void validate() throws BaseException;

}
