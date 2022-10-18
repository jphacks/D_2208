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
import dev.abelab.smartpointer.infrastructure.api.request.RoomJoinRequest;
import dev.abelab.smartpointer.infrastructure.api.response.AccessTokenResponse;
import dev.abelab.smartpointer.property.AuthProperty;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

/**
 * ルーム入室ユースケース
 */
@RequiredArgsConstructor
@Component
public class JoinRoomUseCase {

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final AuthProperty authProperty;

    /**
     * Handle UseCase
     *
     * @param roomId ルームID
     * @param requestBody ルーム入室リクエスト
     * @return アクセストークン
     */
    @Transactional
    public AccessTokenResponse handle(final String roomId, final RoomJoinRequest requestBody) {
        // ルームの取得
        final var room = this.roomRepository.selectById(roomId) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_ROOM));

        // トークンチェック
        if (!room.isTokenValid(requestBody.getToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_ROOM_TOKEN);
        }

        // ユーザ名が使われていないことをチェック
        this.userService.checkIsNameAlreadyUsed(roomId, requestBody.getName());

        // ユーザを作成
        final var user = UserModel.builder() //
            .roomId(roomId) //
            .name(requestBody.getName()) //
            .build();
        this.userRepository.insert(user);

        // アクセストークンを作成
        final var accessToken = Jwts.builder() //
            .setSubject(user.getId()) //
            .setIssuer(this.authProperty.getJwt().getIssuer()) //
            .setIssuedAt(new Date()) //
            .setExpiration(new Date(System.currentTimeMillis() + this.authProperty.getTtl() * 1000)) //
            .signWith(SignatureAlgorithm.HS512, this.authProperty.getJwt().getSecret().getBytes()) //
            .compact();
        return new AccessTokenResponse(this.authProperty.getType(), accessToken);
    }

}
