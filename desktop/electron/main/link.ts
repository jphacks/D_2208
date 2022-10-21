import { BrowserWindow } from "electron";

import type { AppState } from "@/AppState";
import { loadWindow } from "@/window";

let inviteLinkWindow: BrowserWindow | null = null;

export const showInviteLinkWindow = (appState: AppState) => async () => {
  if (appState.state.name !== "CREATED") {
    throw new Error("んなんしとんねん");
  }

  if (inviteLinkWindow === null) {
    inviteLinkWindow = new BrowserWindow({
      width: 600,
      height: 400,
      title: "招待リンク - スマートポインター",
      show: false,
    });

    loadWindow(inviteLinkWindow, "link.html", {
      roomId: appState.state.room.id,
      passcode: appState.state.room.passcode,
    });
  }

  inviteLinkWindow.show();
};
