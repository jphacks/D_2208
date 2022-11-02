import { Client } from "@stomp/stompjs";

const brokerURL = `wss://${location.host}/ws`;

export const stompClient = new Client({
  brokerURL,
});
