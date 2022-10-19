import { app } from "electron";

import { AppState } from "@/AppState";
import { showInviteLinkWindow } from "@/link";
import { createTray } from "@/menu/tray";
import { createRoom } from "@/room";

app.once("ready", () => {
  const appState = new AppState();
  createTray({
    appState,
    handleClickCreateRoom: createRoom({ appState }),
    handleClickShowInviteLink: showInviteLinkWindow(appState),
  });
});

app.once("window-all-closed", () => app.quit());
