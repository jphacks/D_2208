package dev.abelab.smartpointer.helper.graphql

import groovy.transform.TupleConstructor
import lombok.Value

/**
 * GraphQL Query
 *
 * Spockと相性の良いクエリビルダーが無かったため、これで済ませる
 */
@Value
@TupleConstructor
class GraphQLQuery<T> {

    /**
     * オペレーション
     */
    GraphQLOperation operation

    /**
     * 引数
     */
    Map variables

    /**
     * レスポンスタイプ
     *
     * FIXME: responseTypeを指定せず、document()でTを取得したい
     */
    Class<T> responseType

    String document() {
        final variableBlock = this.variables.isEmpty()
            ? ""
            : "(" + this.variables.collect { "${it.key}: ${it.value instanceof String ? "\"" : ""}${it.value}${it.value instanceof String ? "\"" : ""}" }.join(", ") + ")"
        final responseBlock = [String, Number, Boolean].any { this.responseType.isInstance(it) || this.responseType == it }
            ? ""
            : " { ${this.responseType.declaredFields*.name.join(" ")} }"
        return """
            ${this.operation.type.name} {
                ${this.operation.name}${variableBlock}${responseBlock}
            }
        """
    }

}
