package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.Objects;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.auth.LoginUserDetails;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.infrastructure.api.type.Timer;
import dev.abelab.smartpointer.usecase.timer.GetTimerUseCase;
import dev.abelab.smartpointer.usecase.timer.StartTimerUseCase;
import lombok.RequiredArgsConstructor;

/**
 * タイマーコントローラ
 */
@Controller
@RequiredArgsConstructor
public class TimerController {

    private final GetTimerUseCase getTimerUseCase;

    private final StartTimerUseCase startTimerUseCase;

    /**
     * タイマー取得API
     * 
     * @param roomId ルームID
     * @return タイマー
     */
    @QueryMapping
    public Timer getTimer( //
        @Argument final String roomId //
    ) {
        return new Timer(this.getTimerUseCase.handle(roomId));
    }

    /**
     * タイマー開始API
     * 
     * @param loginUser ログインユーザ
     * @param inputTime 入力時間
     * @return タイマー
     */
    @MutationMapping
    public Timer startTimer( //
        @AuthenticationPrincipal final LoginUserDetails loginUser, //
        @Argument final Integer inputTime //
    ) {
        if (Objects.isNull(loginUser)) {
            throw new UnauthorizedException(ErrorCode.USER_NOT_LOGGED_IN);
        }
        return new Timer(this.startTimerUseCase.handle(loginUser.getRoomId(), inputTime));
    }

}
