package dev.abelab.smartpointer.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * プロジェクトプロパティ
 */
@Data
@Configuration
@ConfigurationProperties("project")
public class ProjectProperty {

    /**
     * プロジェクト名
     */
    String name;

    /**
     * バージョン
     */
    String version;

}
