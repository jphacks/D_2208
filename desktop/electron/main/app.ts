import { app } from "electron";

import { AppState } from "./AppState";
import { showInviteLinkWindow } from "./link";
import { createTray } from "./menu/tray";
import { goNext, goPrevious } from "./pagination";
import { sendHidePointer, sendPointerPosition } from "./pointer";
import { createRoom } from "./room";
import { stompClient } from "./stomp";

enum SlideControl {
  NEXT,
  PREVIOUS,
}

const tanDeg = (deg: number) => Math.tan((deg * Math.PI) / 180);

app.once("ready", async () => {
  const appState = new AppState();

  stompClient.activate();

  const handleClickCreateRoom = async () => {
    await createRoom(appState);

    if (appState.state.name !== "CREATED") {
      throw new Error("ぃやなんしとんねん");
    }

    // スライド操作を購読
    stompClient.subscribe(
      `/topic/rooms/${appState.state.room.id}/slides/control`,
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
    stompClient.subscribe(
      `/topic/rooms/${appState.state.room.id}/pointer/control`,
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
          sendPointerPosition({
            x: -tanDeg(body.rotation.alpha) / 2,
            y: -tanDeg(body.rotation.beta) / 2,
          });
        } else {
          sendHidePointer();
        }
      }
    );
  };

  createTray({
    appState,
    handleClickCreateRoom,
    handleClickShowInviteLink: showInviteLinkWindow(appState),
  });
});

app.once("window-all-closed", () => app.quit());
