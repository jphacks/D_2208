import { stompClient } from "./stomp";

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

export const subscribeOrientation = (roomId: string) => {
  window.addEventListener("deviceorientation", (event) => {
    const orientation = {
      alpha: event.alpha!,
      beta: event.beta!,
      gamma: event.gamma!,
    };

    lastOrientation = lastOrientation ?? orientation;

    stompClient.publish({
      destination: `/app/rooms/${roomId}/pointer/control`,
      body: JSON.stringify(sub(orientation, lastOrientation)),
    });
  });
};

export const unsubscribeOrientation = (roomId: string) => {
  lastOrientation = null;
  stompClient.publish({
    destination: `/app/rooms/${roomId}/pointer/disconnect`,
  });
};
