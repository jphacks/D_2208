import { Client, StompSubscription } from "@stomp/stompjs";
import WebSocket from "ws";

import * as controller from "./controller";
import { goNext, goPrevious } from "./pagination";

Object.assign(global, { WebSocket });

const brokerURL =
  process.env["USE_DEV_BACKEND"] === "true"
    ? "ws://localhost:8080/ws"
    : "wss://smartpointer.abelab.dev/ws";

const stompClient = new Client({
  brokerURL,
});

enum SlideControl {
  NEXT,
  PREVIOUS,
}

let slidesControlSubscription: StompSubscription | null = null;
let pointerControlSubscription: StompSubscription | null = null;

export const activate = () =>
  new Promise<undefined>((resolve, reject) => {
    if (stompClient.connected) {
      resolve(undefined);
      console.log("[STOMP] already connected");
      return;
    }
    console.log(`[STOMP] Connecting to ${brokerURL}...`);

    stompClient.onConnect = (frame) => {
      console.log("[STOMP] Connected to broker:", frame);
      resolve(undefined);
    };

    stompClient.onStompError = (frame) => {
      console.error("[STOMP] Broker reported error:");
      console.error(frame.headers["message"]);
      console.error("Additional details:");
      console.error(frame.body);
      reject(new Error(`STOMP error:\n${frame.headers["message"]}`));
    };

    stompClient.activate();
  });

export const listenRoomSubscription = async (roomId: string) => {
  slidesControlSubscription = stompClient.subscribe(
    `/topic/rooms/${roomId}/slides/control`,
    (message) => {
      type Response = {
        control: SlideControl;
      };
      const body = JSON.parse(message.body) as Response;
      if (body.control === SlideControl.NEXT) {
        goNext();
      } else {
        goPrevious();
      }
    }
  );

  // ポインター操作を購読
  pointerControlSubscription = stompClient.subscribe(
    `/topic/rooms/${roomId}/pointer/control`,
    (message) => {
      type Response =
        | {
            isActive: true;
            rotation: {
              alpha: number;
              beta: number;
              gamma: number;
            };
          }
        | {
            isActive: false;
            rotation: null;
          };
      const body = JSON.parse(message.body) as Response;

      if (body.isActive) {
        // TODO: ポインターに認証情報が実装されたら、認証情報を使ってユーザーを特定する
        controller.pointerUpdated(
          {
            id: "user-id",
            name: "user-name",
          },
          {
            alpha: body.rotation.alpha,
            beta: body.rotation.beta,
            gamma: body.rotation.gamma,
          }
        );
      } else {
        // TODO: ポインターに認証情報が実装されたら、認証情報を使ってユーザーを特定する
        controller.pointerDeactivated({
          id: "user-id",
          name: "user-name",
        });
      }
    }
  );
};

export const unsubscribeRoomSubscription = () => {
  if (slidesControlSubscription) {
    slidesControlSubscription.unsubscribe();
    slidesControlSubscription = null;
  }
  if (pointerControlSubscription) {
    pointerControlSubscription.unsubscribe();
    pointerControlSubscription = null;
  }
};
