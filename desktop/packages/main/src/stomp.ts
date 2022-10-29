import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const brokerURL =
  process.env["USE_DEV_BACKEND"] === "true"
    ? "http://localhost:8080/ws"
    : "https://smartpointer.abelab.dev/ws";

export const stompClient = new Client({
  webSocketFactory: () => new SockJS(brokerURL),
  // debug: console.log,
});
