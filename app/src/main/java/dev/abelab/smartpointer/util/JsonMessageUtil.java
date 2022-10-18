package dev.abelab.smartpointer.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.SneakyThrows;

/**
 * JSONメッセージユーティリティ
 */
public class JsonMessageUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper() //
        .registerModule(new JavaTimeModule()) //
        .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

    /**
     * JSON文字列をオブジェクトに変換
     *
     * @param json json
     * @param clazz clazz
     * @return object
     */
    @SneakyThrows
    public static <T> T convertJsonToObject(final String json, final Class<T> clazz) {
        return objectMapper.readValue(json, clazz);
    }

}
