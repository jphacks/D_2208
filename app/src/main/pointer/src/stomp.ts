import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client/dist/sockjs";

export const stompClient = new Client({
  webSocketFactory: () => new SockJS(`/ws`),
});
