package dev.abelab.smartpointer.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 認証プロパティ
 */
@Data
@Configuration
@ConfigurationProperties("auth")
public class AuthProperty {

    /**
     * TTL[s]
     */
    Integer ttl;

    /**
     * トークンタイプ
     */
    String tokenType;

    /**
     * JWT
     */
    JWT jwt;

    @Data
    public static class JWT {

        /**
         * Secret Key
         */
        String secret;

        /**
         * Issuer
         */
        String issuer;

    }

}
