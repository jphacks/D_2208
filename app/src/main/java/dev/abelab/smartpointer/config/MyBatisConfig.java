package dev.abelab.smartpointer.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisの設定
 */
@MapperScan("dev.abelab.smartpointer.infrastructure.db.mapper")
@Configuration
public class MyBatisConfig {
}
