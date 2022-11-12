package dev.abelab.smartpointer.domain.model;

import org.apache.commons.net.util.Base64;

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
     * ID
     */
    private String id;

    /**
     * ルームID
     */
    private String roomId;

    /**
     * ファイルのバイナリ
     */
    private byte[] content;

    public FileModel(final String id, final String roomId, final String content) {
        this.id = id;
        this.roomId = roomId;
        this.content = Base64.decodeBase64(content);
    }

}
