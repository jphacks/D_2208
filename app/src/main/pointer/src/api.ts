import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import { ExecutionResult, print } from "graphql";
import { GraphQLClient } from "graphql-request";
import { RemoveIndex, RequestOptions } from "graphql-request/dist/types";
import { createClient, Sink } from "graphql-ws";

const graphqlHttpEndpoint = `${location.origin}/graphql`;

const graphqlHttpClient = new GraphQLClient(graphqlHttpEndpoint);

export const request = <
  T = unknown,
  V extends Record<string, unknown> = Record<string, unknown>
>({
  query,
  variables,
}: {
  query: TypedDocumentNode<T, V>;
} & (V extends Record<string | number | symbol, never>
  ? { variables?: V }
  : keyof RemoveIndex<V> extends never
  ? { variables?: V }
  : { variables: V })): Promise<T> => {
  return graphqlHttpClient.request<T, V>({
    document: query,
    variables: variables,
  } as RequestOptions<V, T>);
};

const graphqlWsEndpoint = `wss://${location.host}/graphql-ws`;

const rawGraphqlWsClient = createClient({
  url: graphqlWsEndpoint,
  lazy: true,
});

export const subscribe = <
  T = unknown,
  V extends Record<string, unknown> = Record<string, unknown>
>(
  {
    query,
    variables,
  }: {
    query: TypedDocumentNode<T, V>;
  } & (V extends Record<string | number | symbol, never>
    ? { variables?: V }
    : keyof RemoveIndex<V> extends never
    ? { variables?: V }
    : { variables: V }),
  sink: Sink<ExecutionResult<T>>
) => {
  rawGraphqlWsClient.subscribe(
    {
      query: print(query),
      variables,
    },
    sink
  );
};
