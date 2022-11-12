import { BrowserWindow } from "electron";

import { model } from "@/model";
import { loadFile } from "@/utils/window/loadFile";

let inviteLinkWindow: BrowserWindow | null = null;

export const inviteLink = {
  show: async () => {
    const state = model.state;

    if (state.status !== "CREATED") {
      throw new Error(
        "Cannot show invite link window when not in CREATED state"
      );
    }

    if (inviteLinkWindow === null || inviteLinkWindow.isDestroyed()) {
      inviteLinkWindow = new BrowserWindow({
        width: 600,
        height: 400,
        title: "招待リンク - スマートポインター",
        show: false,
      });

      await loadFile(inviteLinkWindow, "link.html", {
        roomId: state.room.id,
        passcode: state.room.passcode,
        origin:
          process.env["USE_DEV_BACKEND"] === "true"
            ? "https://DEV_HOST"
            : "https://smartpointer.abelab.dev",
      });
    }

    inviteLinkWindow.removeMenu();
    inviteLinkWindow.show();
  },

  close: () => {
    inviteLinkWindow?.close();
  },
};
