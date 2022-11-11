package dev.abelab.smartpointer.usecase.pointer;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.PointerControlModel;
import dev.abelab.smartpointer.domain.model.UserModel;
import lombok.RequiredArgsConstructor;

/**
 * ポインター操作ユースケース
 */
@RequiredArgsConstructor
@Component
public class MovePointerUseCase {

    /**
     * Handle UseCase
     * 
     * @param roomId ルームID
     * @param loginUser ログインユーザ
     * @param alpha α値
     * @param beta β値
     * @param gamma γ値
     * @return ポインター操作
     */
    public PointerControlModel handle(final String roomId, final UserModel loginUser, final Double alpha, final Double beta,
        final Double gamma) {
        // 60rps/userでテーブルロックするとパフォーマンスが落ちるので
        // ルームの存在チェックは行わない
        return new PointerControlModel(roomId, loginUser, alpha, beta, gamma);
    }

}
