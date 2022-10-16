package dev.abelab.smartpointer.domain.model;

import java.util.Base64;
import java.util.UUID;

import org.springframework.security.crypto.keygen.KeyGenerators;

import dev.abelab.smartpointer.infrastructure.db.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ルームモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomModel {

    /**
     * ルームID
     */
    @Builder.Default
    String id = UUID.randomUUID().toString();

    /**
     * トークン
     */
    @Builder.Default
    String token = Base64.getUrlEncoder().encodeToString(KeyGenerators.secureRandom(32).generateKey());

    public RoomModel(final Room room) {
        this.id = room.getId();
        this.token = room.getToken();
    }

}
