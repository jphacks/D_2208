package dev.abelab.smartpointer.domain.service;

import org.springframework.stereotype.Service;

import dev.abelab.smartpointer.exception.BadRequestException;
import dev.abelab.smartpointer.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

/**
 * タイマーサービス
 */
@RequiredArgsConstructor
@Service
public class TimerService {

    /**
     * 入力時間が有効かチェック
     * 
     * @param inputTime 入力時間
     */
    public void checkIsInputTimeValid(final Integer inputTime) throws BadRequestException {
        if (inputTime < 1 || inputTime > 3600) {
            throw new BadRequestException(ErrorCode.INVALID_TIMER_INPUT_TIME);
        }
    }

}
