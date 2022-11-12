/* eslint-disable */
import * as types from './graphql';
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';

const documents = {
    "\n          mutation JoinRoom(\n            $roomId: ID!\n            $passcode: String!\n            $userName: String!\n          ) {\n            joinRoom(\n              roomId: $roomId\n              passcode: $passcode\n              userName: $userName\n            ) {\n              tokenType\n              accessToken\n              ttl\n              user {\n                id\n              }\n            }\n          }\n        ": types.JoinRoomDocument,
    "\n          mutation GoNextSlide($accessToken: String!) {\n            goNextSlide(accessToken: $accessToken)\n          }\n        ": types.GoNextSlideDocument,
    "\n          mutation GoPreviousSlide($accessToken: String!) {\n            goPreviousSlide(accessToken: $accessToken)\n          }\n        ": types.GoPreviousSlideDocument,
    "\n              mutation StartTimer($inputTime: Int!, $accessToken: String!) {\n                startTimer(inputTime: $inputTime, accessToken: $accessToken) {\n                  status\n                }\n              }\n            ": types.StartTimerDocument,
    "\n              mutation ResumeTime($accessToken: String!) {\n                resumeTimer(accessToken: $accessToken) {\n                  status\n                }\n              }\n            ": types.ResumeTimeDocument,
    "\n              mutation PauseTimer($accessToken: String!) {\n                pauseTimer(accessToken: $accessToken) {\n                  status\n                }\n              }\n            ": types.PauseTimerDocument,
    "\n          mutation ResetTimer($accessToken: String!) {\n            resetTimer(accessToken: $accessToken) {\n              status\n            }\n          }\n        ": types.ResetTimerDocument,
    "\n          subscription SubscribeTimer($roomId: ID!) {\n            subscribeToTimer(roomId: $roomId) {\n              status\n              inputTime\n              remainingTimeAtPaused\n              finishAt\n            }\n          }\n        ": types.SubscribeTimerDocument,
    "\n          query GetTimer($roomId: ID!) {\n            getTimer(roomId: $roomId) {\n              status\n              inputTime\n              remainingTimeAtPaused\n              finishAt\n            }\n          }\n        ": types.GetTimerDocument,
    "\n          mutation MovePointer(\n            $alpha: Float\n            $beta: Float\n            $gamma: Float\n            $accessToken: String!\n          ) {\n            movePointer(\n              alpha: $alpha\n              beta: $beta\n              gamma: $gamma\n              accessToken: $accessToken\n            ) {\n              user {\n                id\n              }\n            }\n          }\n        ": types.MovePointerDocument,
    "\n        mutation DisconnectPointer($accessToken: String!) {\n          disconnectPointer(accessToken: $accessToken) {\n            id\n          }\n        }\n      ": types.DisconnectPointerDocument,
};

export function graphql(source: "\n          mutation JoinRoom(\n            $roomId: ID!\n            $passcode: String!\n            $userName: String!\n          ) {\n            joinRoom(\n              roomId: $roomId\n              passcode: $passcode\n              userName: $userName\n            ) {\n              tokenType\n              accessToken\n              ttl\n              user {\n                id\n              }\n            }\n          }\n        "): (typeof documents)["\n          mutation JoinRoom(\n            $roomId: ID!\n            $passcode: String!\n            $userName: String!\n          ) {\n            joinRoom(\n              roomId: $roomId\n              passcode: $passcode\n              userName: $userName\n            ) {\n              tokenType\n              accessToken\n              ttl\n              user {\n                id\n              }\n            }\n          }\n        "];
export function graphql(source: "\n          mutation GoNextSlide($accessToken: String!) {\n            goNextSlide(accessToken: $accessToken)\n          }\n        "): (typeof documents)["\n          mutation GoNextSlide($accessToken: String!) {\n            goNextSlide(accessToken: $accessToken)\n          }\n        "];
export function graphql(source: "\n          mutation GoPreviousSlide($accessToken: String!) {\n            goPreviousSlide(accessToken: $accessToken)\n          }\n        "): (typeof documents)["\n          mutation GoPreviousSlide($accessToken: String!) {\n            goPreviousSlide(accessToken: $accessToken)\n          }\n        "];
export function graphql(source: "\n              mutation StartTimer($inputTime: Int!, $accessToken: String!) {\n                startTimer(inputTime: $inputTime, accessToken: $accessToken) {\n                  status\n                }\n              }\n            "): (typeof documents)["\n              mutation StartTimer($inputTime: Int!, $accessToken: String!) {\n                startTimer(inputTime: $inputTime, accessToken: $accessToken) {\n                  status\n                }\n              }\n            "];
export function graphql(source: "\n              mutation ResumeTime($accessToken: String!) {\n                resumeTimer(accessToken: $accessToken) {\n                  status\n                }\n              }\n            "): (typeof documents)["\n              mutation ResumeTime($accessToken: String!) {\n                resumeTimer(accessToken: $accessToken) {\n                  status\n                }\n              }\n            "];
export function graphql(source: "\n              mutation PauseTimer($accessToken: String!) {\n                pauseTimer(accessToken: $accessToken) {\n                  status\n                }\n              }\n            "): (typeof documents)["\n              mutation PauseTimer($accessToken: String!) {\n                pauseTimer(accessToken: $accessToken) {\n                  status\n                }\n              }\n            "];
export function graphql(source: "\n          mutation ResetTimer($accessToken: String!) {\n            resetTimer(accessToken: $accessToken) {\n              status\n            }\n          }\n        "): (typeof documents)["\n          mutation ResetTimer($accessToken: String!) {\n            resetTimer(accessToken: $accessToken) {\n              status\n            }\n          }\n        "];
export function graphql(source: "\n          subscription SubscribeTimer($roomId: ID!) {\n            subscribeToTimer(roomId: $roomId) {\n              status\n              inputTime\n              remainingTimeAtPaused\n              finishAt\n            }\n          }\n        "): (typeof documents)["\n          subscription SubscribeTimer($roomId: ID!) {\n            subscribeToTimer(roomId: $roomId) {\n              status\n              inputTime\n              remainingTimeAtPaused\n              finishAt\n            }\n          }\n        "];
export function graphql(source: "\n          query GetTimer($roomId: ID!) {\n            getTimer(roomId: $roomId) {\n              status\n              inputTime\n              remainingTimeAtPaused\n              finishAt\n            }\n          }\n        "): (typeof documents)["\n          query GetTimer($roomId: ID!) {\n            getTimer(roomId: $roomId) {\n              status\n              inputTime\n              remainingTimeAtPaused\n              finishAt\n            }\n          }\n        "];
export function graphql(source: "\n          mutation MovePointer(\n            $alpha: Float\n            $beta: Float\n            $gamma: Float\n            $accessToken: String!\n          ) {\n            movePointer(\n              alpha: $alpha\n              beta: $beta\n              gamma: $gamma\n              accessToken: $accessToken\n            ) {\n              user {\n                id\n              }\n            }\n          }\n        "): (typeof documents)["\n          mutation MovePointer(\n            $alpha: Float\n            $beta: Float\n            $gamma: Float\n            $accessToken: String!\n          ) {\n            movePointer(\n              alpha: $alpha\n              beta: $beta\n              gamma: $gamma\n              accessToken: $accessToken\n            ) {\n              user {\n                id\n              }\n            }\n          }\n        "];
export function graphql(source: "\n        mutation DisconnectPointer($accessToken: String!) {\n          disconnectPointer(accessToken: $accessToken) {\n            id\n          }\n        }\n      "): (typeof documents)["\n        mutation DisconnectPointer($accessToken: String!) {\n          disconnectPointer(accessToken: $accessToken) {\n            id\n          }\n        }\n      "];

export function graphql(source: string): unknown;
export function graphql(source: string) {
  return (documents as any)[source] ?? {};
}

export type DocumentType<TDocumentNode extends DocumentNode<any, any>> = TDocumentNode extends DocumentNode<  infer TType,  any>  ? TType  : never;