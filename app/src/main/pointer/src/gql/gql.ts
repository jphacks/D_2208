/* eslint-disable */
import * as types from './graphql';
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';

const documents = {
    "\n          mutation JoinRoom(\n            $roomId: ID!\n            $passcode: String!\n            $userName: String!\n          ) {\n            joinRoom(\n              roomId: $roomId\n              passcode: $passcode\n              userName: $userName\n            ) {\n              tokenType\n              accessToken\n              ttl\n            }\n          }\n        ": types.JoinRoomDocument,
    "\n          mutation GoNextSlide($accessToken: String!) {\n            goNextSlide(accessToken: $accessToken)\n          }\n        ": types.GoNextSlideDocument,
    "\n          mutation GoPreviousSlide($accessToken: String!) {\n            goPreviousSlide(accessToken: $accessToken)\n          }\n        ": types.GoPreviousSlideDocument,
};

export function graphql(source: "\n          mutation JoinRoom(\n            $roomId: ID!\n            $passcode: String!\n            $userName: String!\n          ) {\n            joinRoom(\n              roomId: $roomId\n              passcode: $passcode\n              userName: $userName\n            ) {\n              tokenType\n              accessToken\n              ttl\n            }\n          }\n        "): (typeof documents)["\n          mutation JoinRoom(\n            $roomId: ID!\n            $passcode: String!\n            $userName: String!\n          ) {\n            joinRoom(\n              roomId: $roomId\n              passcode: $passcode\n              userName: $userName\n            ) {\n              tokenType\n              accessToken\n              ttl\n            }\n          }\n        "];
export function graphql(source: "\n          mutation GoNextSlide($accessToken: String!) {\n            goNextSlide(accessToken: $accessToken)\n          }\n        "): (typeof documents)["\n          mutation GoNextSlide($accessToken: String!) {\n            goNextSlide(accessToken: $accessToken)\n          }\n        "];
export function graphql(source: "\n          mutation GoPreviousSlide($accessToken: String!) {\n            goPreviousSlide(accessToken: $accessToken)\n          }\n        "): (typeof documents)["\n          mutation GoPreviousSlide($accessToken: String!) {\n            goPreviousSlide(accessToken: $accessToken)\n          }\n        "];

export function graphql(source: string): unknown;
export function graphql(source: string) {
  return (documents as any)[source] ?? {};
}

export type DocumentType<TDocumentNode extends DocumentNode<any, any>> = TDocumentNode extends DocumentNode<  infer TType,  any>  ? TType  : never;