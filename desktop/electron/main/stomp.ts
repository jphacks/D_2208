import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const brokerURL =
  process.env["NODE_ENV"] === "development"
    ? "http://localhost:8080/ws"
    : "https://smartpointer.abelab.dev/ws";

export const stompClient = new Client({
  webSocketFactory: () => new SockJS(brokerURL),
  debug: console.log,
});
