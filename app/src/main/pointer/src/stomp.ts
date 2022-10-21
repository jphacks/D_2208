import { Client } from "@stomp/stompjs";

export const client = new Client({
  brokerURL: "/ws",
});
