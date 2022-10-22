package dev.abelab.smartpointer.util;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * バリデーションユーティリティ
 */
public class ValidationUtil {

    /**
     * 数値が範囲に収まるかチェック
     *
     * @param number 数値
     * @param min 最小値
     * @param max 最大値
     * @return バリデーション結果
     */
    public static Boolean checkNumberSize(final Integer number, final Integer min, final Integer max) {
        if (Objects.isNull(number)) {
            return false;
        }

        return (Objects.isNull(min) || number >= min) && (Objects.isNull(max) || number <= max);
    }

    /**
     * 文字列の長さが範囲に収まるかチェック
     *
     * @param string 文字列
     * @param min 最小文字数
     * @param max 最大文字数
     * @return バリデーション結果
     */
    public static Boolean checkStringLength(final String string, final Integer min, final Integer max) {
        if (Objects.isNull(string)) {
            return false;
        }

        if (StringUtils.isBlank(string) && min != 0) {
            return false;
        } else {
            return (Objects.isNull(min) || string.length() >= min) && (Objects.isNull(max) || string.length() <= max);
        }
    }

    /**
     * 英数字のみの文字列かチェック
     * 
     * @param string 文字列
     * @return バリデーション結果
     */
    public static Boolean checkIsAlphanumeric(final String string) {
        if (Objects.isNull(string)) {
            return false;
        }

        return string.matches("[A-Za-z\\d]*");
    }

}
