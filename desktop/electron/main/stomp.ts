import { Client } from "@stomp/stompjs";
import { WebSocket } from "ws";

Object.assign(global, { WebSocket });

const brokerURL =
  process.env["NODE_ENV"] === "development"
    ? "ws://localhost:8080/ws"
    : "wss://smartpointer.abelab.dev/ws";

export const stompClient = new Client({ brokerURL });
