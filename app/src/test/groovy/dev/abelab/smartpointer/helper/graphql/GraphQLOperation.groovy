package dev.abelab.smartpointer.helper.graphql

import groovy.transform.TupleConstructor

/**
 * GraphQL Operation
 */
@TupleConstructor
enum GraphQLOperation {

    HEALTH_CHECK("health", GraphQLOperationType.QUERY),

    CREATE_ROOM("createRoom", GraphQLOperationType.MUTATION),

    DELETE_ROOM("deleteRoom", GraphQLOperationType.MUTATION),

    JOIN_ROOM("joinRoom", GraphQLOperationType.MUTATION),

    final String name

    final GraphQLOperationType type

    @TupleConstructor
    private static enum GraphQLOperationType {

        QUERY("query"),

        MUTATION("mutation"),

        SUBSCRIPTION("subscription"),

        final String name
    }

}