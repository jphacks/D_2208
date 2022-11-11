package dev.abelab.smartpointer.util;

import org.springframework.stereotype.Component;

import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.domain.repository.UserRepository;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.property.AuthProperty;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

/**
 * 認証ユーティリティ
 */
@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final UserRepository userRepository;

    private final AuthProperty authProperty;

    /**
     * アクセストークンからログインユーザを取得
     * 
     * @param accessToken アクセストークン
     * @return ログインユーザ
     */
    public UserModel getLoginUser(final String accessToken) {
        try {
            final var subject = Jwts.parser() //
                .setSigningKey(this.authProperty.getJwt().getSecret().getBytes()) //
                .requireIssuer(this.authProperty.getJwt().getIssuer()) //
                .parseClaimsJws(accessToken) //
                .getBody() //
                .getSubject();

            return this.userRepository.selectById(subject) //
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN));
        } catch (final Exception ignored) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

}
