import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import { ExecutionResult, print } from "graphql";
import { GraphQLClient } from "graphql-request";
import { RemoveIndex, RequestOptions } from "graphql-request/dist/types";
import { Client, createClient, Sink } from "graphql-ws";

const graphqlHttpEndpoint = `${location.origin}/graphql`;

const graphqlHttpClient = new GraphQLClient(graphqlHttpEndpoint);

export const requestHttp = <
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

let graphqlWsClient: Client | null = null;

export const initializeWsClient = (accessToken: string) => {
  graphqlWsClient = createClient({
    url: graphqlWsEndpoint,
    connectionParams: {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    },
    lazy: false,
  });
};

export const requestWs = <
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
  if (!graphqlWsClient) {
    throw new Error("graphql-ws client not initialized");
  }

  graphqlWsClient.subscribe(
    {
      query: print(query),
      variables,
    },
    sink
  );
};
