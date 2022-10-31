import { Client, StompSubscription } from "@stomp/stompjs";
import SockJS from "sockjs-client";

import * as controller from "./controller";
import { goNext, goPrevious } from "./pagination";

const brokerURL =
  process.env["USE_DEV_BACKEND"] === "true"
    ? "http://localhost:8080/ws"
    : "https://smartpointer.abelab.dev/ws";

const stompClient = new Client({
  webSocketFactory: () => new SockJS(brokerURL),
});

enum SlideControl {
  NEXT,
  PREVIOUS,
}

const tanDeg = (deg: number) => Math.tan((deg * Math.PI) / 180);

let slidesControlSubscription: StompSubscription | null = null;
let pointerControlSubscription: StompSubscription | null = null;

export const activate = () => {
  stompClient.activate();
};

export const listenRoomSubscription = (roomId: string) => {
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
        controller.pointerUpdated("userId", {
          x: -tanDeg(body.rotation.alpha) / 2,
          y: -tanDeg(body.rotation.beta) / 2,
        });
      } else {
        // TODO: ポインターに認証情報が実装されたら、認証情報を使ってユーザーを特定する
        controller.pointerDeactivated("userId");
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
