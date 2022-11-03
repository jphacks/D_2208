import { BrowserWindow } from "electron";

import { getState } from "@/model";

import { loadFile } from "./loadFile";

let inviteLinkWindow: BrowserWindow | null = null;

export const showInviteLinkWindow = async () => {
  const state = getState();

  if (state.status !== "CREATED") {
    throw new Error("Cannot show invite link window when not in CREATED state");
  }

  if (inviteLinkWindow === null || inviteLinkWindow.isDestroyed()) {
    inviteLinkWindow = new BrowserWindow({
      width: 600,
      height: 400,
      title: "招待リンク - スマートポインター",
      show: false,
    });

    await loadFile(inviteLinkWindow, "link.html", {
      roomId: state.room.roomId,
      passcode: state.room.passcode,
    });
  }

  inviteLinkWindow.show();
};
