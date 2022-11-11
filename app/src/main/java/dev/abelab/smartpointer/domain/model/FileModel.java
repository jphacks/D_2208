package dev.abelab.smartpointer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ファイルモデル
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileModel {

    /**
     * UUID
     */
    private String uuid;

    /**
     * ファイルのバイナリ
     */
    private byte[] content;

}
