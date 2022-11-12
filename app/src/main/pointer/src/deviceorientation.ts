// import { stompClient } from "./stomp";

import { requestWs } from "@/api";
import { graphql } from "@/gql";

// eslint-disable-next-line @typescript-eslint/no-empty-function
const noop = () => {};

const messagePleasingWhenDeny = [
  "使用するには許可してください🥺",
  "（リロードしてください）",
].join("\n");

/**
 * for iOS Safari 13+
 */
export const requestPermission = async () => {
  if (
    DeviceMotionEvent &&
    // @ts-expect-error iOS 13+ にしかない
    typeof DeviceMotionEvent.requestPermission === "function"
  ) {
    // @ts-expect-error iOS 13+ にしかない
    const permission = await DeviceMotionEvent.requestPermission();
    if (permission === "denied") {
      alert(messagePleasingWhenDeny);
    }
  }

  if (
    DeviceOrientationEvent &&
    // @ts-expect-error iOS 13+ にしかない
    typeof DeviceOrientationEvent.requestPermission === "function"
  ) {
    // @ts-expect-error iOS 13+ にしかない
    const permission = await DeviceOrientationEvent.requestPermission();
    if (permission === "denied") {
      alert(messagePleasingWhenDeny);
    }
  }
};

const orientationKey = ["alpha", "beta", "gamma"] as const;

type Orientation = { [K in typeof orientationKey[number]]: number };

const sub = (a: Orientation, b: Orientation): Orientation => ({
  alpha: a.alpha - b.alpha,
  beta: a.beta - b.beta,
  gamma: a.gamma - b.gamma,
});

let lastOrientation: Orientation | null = null;
let handler: ((orientation: DeviceOrientationEvent) => void) | null = null;

export const subscribeOrientation = (accessToken: string) => {
  if (handler) {
    return;
  }
  handler = (event: DeviceOrientationEvent) => {
    const orientation = {
      alpha: event.alpha!,
      beta: event.beta!,
      gamma: event.gamma!,
    };

    lastOrientation = lastOrientation ?? orientation;

    requestWs(
      {
        query: graphql(/* GraphQL */ `
          mutation MovePointer(
            $alpha: Float
            $beta: Float
            $gamma: Float
            $accessToken: String!
          ) {
            movePointer(
              alpha: $alpha
              beta: $beta
              gamma: $gamma
              accessToken: $accessToken
            ) {
              user {
                id
              }
            }
          }
        `),
        variables: {
          ...sub(orientation, lastOrientation),
          accessToken,
        },
      },
      {
        next: noop,
        error: noop,
        complete: noop,
      }
    );
  };
  window.addEventListener("deviceorientation", handler);
};

export const unsubscribeOrientation = (accessToken: string) => {
  lastOrientation = null;
  if (handler) {
    window.removeEventListener("deviceorientation", handler);
    handler = null;
  }

  requestWs(
    {
      query: graphql(/* GraphQL */ `
        mutation DisconnectPointer($accessToken: String!) {
          disconnectPointer(accessToken: $accessToken) {
            id
          }
        }
      `),
      variables: {
        accessToken,
      },
    },
    {
      next: noop,
      error: noop,
      complete: noop,
    }
  );
};
