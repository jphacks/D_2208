import { Client } from "@stomp/stompjs";

const brokerURL = `wss://${location.host}/ws`;

export const stompClient = new Client({
  brokerURL,
  debug: console.log,
});

export const activate = () =>
  new Promise<undefined>((resolve, reject) => {
    if (stompClient.connected) {
      resolve(undefined);
    } else {
      stompClient.onConnect = (frame) => {
        console.log("Connected to broker:", frame);
        resolve(undefined);
      };

      stompClient.onStompError = (frame) => {
        console.error("Broker reported error:");
        console.error(frame.headers["message"]);
        console.error("Additional details:");
        console.error(frame.body);
        reject(new Error(`STOMP error:\n${frame.headers["message"]}`));
      };
    }

    stompClient.activate();
  });
