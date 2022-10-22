import { BrowserWindow } from "electron";

import type { AppState } from "./AppState";
import { loadWindow } from "./window";

let inviteLinkWindow: BrowserWindow | null = null;

export const showInviteLinkWindow = async (appState: AppState) => {
  if (appState.state.name !== "CREATED") {
    throw new Error("んなんしとんねん");
  }

  inviteLinkWindow = new BrowserWindow({
    width: 600,
    height: 400,
    title: "招待リンク - スマートポインター",
    show: false,
  });

  await loadWindow(inviteLinkWindow, "link.html", {
    roomId: appState.state.room.id,
    passcode: appState.state.room.passcode,
  });

  inviteLinkWindow.show();
};

export const toggleInviteLinkWindowDevTools = () => {
  if (inviteLinkWindow === null) {
    return;
  }
  inviteLinkWindow.webContents.toggleDevTools();
};
