package dev.abelab.smartpointer.usecase;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.UserModel;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.domain.repository.UserRepository;
import dev.abelab.smartpointer.domain.service.UserService;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.exception.UnauthorizedException;
import dev.abelab.smartpointer.infrastructure.api.request.LoginRequest;
import dev.abelab.smartpointer.property.AuthProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

/**
 * ログインユースケース
 */
@RequiredArgsConstructor
@Component
public class LoginUseCase {

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final AuthProperty authProperty;

    /**
     * Handle UseCase
     * 
     * @param requestBody ログインリクエスト
     * @return access token
     */
    @Transactional
    public String handle(final LoginRequest requestBody) {
        // ルームの取得
        final var room = this.roomRepository.selectById(requestBody.getRoomId()) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ROOM));

        // トークンチェック
        if (!room.isTokenValid(requestBody.getToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_ROOM_TOKEN);
        }

        // ユーザ名が使われていないことをチェック
        this.userService.checkIsNameAlreadyUsed(requestBody.getRoomId(), requestBody.getName());

        // ユーザを作成
        final var user = UserModel.builder() //
            .roomId(requestBody.getRoomId()) //
            .name(requestBody.getName()) //
            .build();
        this.userRepository.insert(user);

        return Jwts.builder() //
            .setSubject(user.getId()) //
            .setIssuer(this.authProperty.getJwt().getIssuer()) //
            .setIssuedAt(new Date()) //
            .setExpiration(new Date(System.currentTimeMillis() + this.authProperty.getTtl() * 1000)) //
            .signWith(SignatureAlgorithm.HS512, this.authProperty.getJwt().getSecret().getBytes()) //
            .compact();
    }

}
