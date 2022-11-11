import type { TypedDocumentNode } from "@graphql-typed-document-node/core";
import { ExecutionResult, print } from "graphql";
import { GraphQLClient } from "graphql-request";
import { RemoveIndex, RequestOptions } from "graphql-request/dist/types";
import { createClient, Sink, Client } from "graphql-ws";
import { WebSocket } from "ws";

const httpOrigin =
  process.env["USE_DEV_BACKEND"] === "true"
    ? "http://localhost:8080"
    : "https://smartpointer.abelab.dev";

const graphqlHttpEndpoint = `${httpOrigin}/graphql`;

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
const wsOrigin =
  process.env["USE_DEV_BACKEND"] === "true"
    ? "ws://localhost:8080"
    : "wss://smartpointer.abelab.dev";

const graphqlWsEndpoint = `${wsOrigin}/graphql-ws`;

let graphqlWsClient: Client | null = null;

export const initializeWsClient = () => {
  graphqlWsClient = createClient({
    url: graphqlWsEndpoint,
    webSocketImpl: WebSocket,
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
