/* eslint-disable */
import * as types from './graphql';
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';

const documents = {
    "\n        mutation CreateRoom {\n          createRoom {\n            id\n            passcode\n          }\n        }\n      ": types.CreateRoomDocument,
    "\n          subscription SubscribeToSlideControl($roomId: ID!) {\n            subscribeToSlideControl(roomId: $roomId)\n          }\n        ": types.SubscribeToSlideControlDocument,
    "\n          subscription SubscribeToPointer($roomId: ID!) {\n            subscribeToPointer(roomId: $roomId) {\n              orientation {\n                alpha\n                beta\n                gamma\n              }\n              user {\n                id\n                name\n              }\n            }\n          }\n        ": types.SubscribeToPointerDocument,
    "\n          subscription SubscribeToPointerDisconnectEvent($roomId: ID!) {\n            subscribeToPointerDisconnectEvent(roomId: $roomId) {\n              id\n            }\n          }\n        ": types.SubscribeToPointerDisconnectEventDocument,
    "\n        mutation DeleteRoom($roomId: ID!) {\n          deleteRoom(roomId: $roomId)\n        }\n      ": types.DeleteRoomDocument,
};

export function graphql(source: "\n        mutation CreateRoom {\n          createRoom {\n            id\n            passcode\n          }\n        }\n      "): (typeof documents)["\n        mutation CreateRoom {\n          createRoom {\n            id\n            passcode\n          }\n        }\n      "];
export function graphql(source: "\n          subscription SubscribeToSlideControl($roomId: ID!) {\n            subscribeToSlideControl(roomId: $roomId)\n          }\n        "): (typeof documents)["\n          subscription SubscribeToSlideControl($roomId: ID!) {\n            subscribeToSlideControl(roomId: $roomId)\n          }\n        "];
export function graphql(source: "\n          subscription SubscribeToPointer($roomId: ID!) {\n            subscribeToPointer(roomId: $roomId) {\n              orientation {\n                alpha\n                beta\n                gamma\n              }\n              user {\n                id\n                name\n              }\n            }\n          }\n        "): (typeof documents)["\n          subscription SubscribeToPointer($roomId: ID!) {\n            subscribeToPointer(roomId: $roomId) {\n              orientation {\n                alpha\n                beta\n                gamma\n              }\n              user {\n                id\n                name\n              }\n            }\n          }\n        "];
export function graphql(source: "\n          subscription SubscribeToPointerDisconnectEvent($roomId: ID!) {\n            subscribeToPointerDisconnectEvent(roomId: $roomId) {\n              id\n            }\n          }\n        "): (typeof documents)["\n          subscription SubscribeToPointerDisconnectEvent($roomId: ID!) {\n            subscribeToPointerDisconnectEvent(roomId: $roomId) {\n              id\n            }\n          }\n        "];
export function graphql(source: "\n        mutation DeleteRoom($roomId: ID!) {\n          deleteRoom(roomId: $roomId)\n        }\n      "): (typeof documents)["\n        mutation DeleteRoom($roomId: ID!) {\n          deleteRoom(roomId: $roomId)\n        }\n      "];

export function graphql(source: string): unknown;
export function graphql(source: string) {
  return (documents as any)[source] ?? {};
}

export type DocumentType<TDocumentNode extends DocumentNode<any, any>> = TDocumentNode extends DocumentNode<  infer TType,  any>  ? TType  : never;