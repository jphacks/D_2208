package dev.abelab.smartpointer.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * GCPプロパティ
 */
@Data
@Configuration
@ConfigurationProperties("gcp")
public class GcpProperty {

    /**
     * プロジェクトID
     */
    String projectId;

    /**
     * 資格情報のファイルパス
     */
    String credentialsPath;

    /**
     * Cloud Storage
     */
    CloudStorage cloudStorage;

    @Data
    public static class CloudStorage {

        /**
         * 有効フラグ
         */
        boolean enabled;

        /**
         * バケット名
         */
        String bucketName;

    }

}
