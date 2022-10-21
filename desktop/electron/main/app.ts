import { app } from "electron";

import { AppState } from "@/AppState";
import { showInviteLinkWindow } from "@/link";
import { createTray } from "@/menu/tray";
import { createRoom } from "@/room";
import { stompClient } from "@/stomp";

app.once("ready", () => {
  const appState = new AppState();

  stompClient.activate();

  createTray({
    appState,
    handleClickCreateRoom: createRoom(appState),
    handleClickShowInviteLink: showInviteLinkWindow(appState),
  });
});

app.once("window-all-closed", () => app.quit());
