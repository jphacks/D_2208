import { Client } from "@stomp/stompjs";

export const stompClient = new Client({
  brokerURL: `ws${location.protocol === "https:" ? "s" : ""}://${
    location.host
  }/ws`,
});
