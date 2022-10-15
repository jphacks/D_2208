package dev.abelab.smartpointer.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;

/**
 * テストの設定
 */
@Profile("test")
@Configuration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // テスト用DBをマイグレーションするのは面倒なので、テスト開始前に初期化する
            flyway.clean();
            flyway.migrate();
        };
    }

}
