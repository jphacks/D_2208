import { BrowserWindow } from "electron";

import { hideInviteLink } from "@/controller";
import { State } from "@/types";

import { loadFile } from "./loadFile";

let inviteLinkWindow: BrowserWindow | null = null;

export const renderInviteLinkWindow = async (state: State) => {
  if (state.status !== "CREATED" || !state.showInviteLink) {
    if (inviteLinkWindow && !inviteLinkWindow.isDestroyed()) {
      inviteLinkWindow.close();
    }
    inviteLinkWindow = null;
    return;
  }

  if (inviteLinkWindow !== null && !inviteLinkWindow.isDestroyed()) {
    inviteLinkWindow.focus();
    return;
  }

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

  inviteLinkWindow.show();

  inviteLinkWindow.on("closed", () => {
    hideInviteLink();
  });
};
