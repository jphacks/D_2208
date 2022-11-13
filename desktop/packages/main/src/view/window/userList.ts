import { User } from "@smartpointer-desktop/shared";
import { app, BrowserWindow, ipcMain } from "electron";
import { join } from "node:path";

import { controller } from "@/controller";
import { loadFile } from "@/utils/window/loadFile";

let userListWindow: BrowserWindow | null = null;

export const userList = {
  show: async () => {
    if (userListWindow === null || userListWindow.isDestroyed()) {
      userListWindow = new BrowserWindow({
        width: 400,
        height: 600,
        title: "参加者一覧 - スマートポインター",
        show: false,
        webPreferences: {
          preload: join(
            app.getAppPath(),
            "packages",
            "preload",
            "dist",
            "index.cjs"
          ),
        },
      });

      await loadFile(userListWindow, "userList.html");
    }

    userListWindow.removeMenu();
    userListWindow.show();
  },

  close: () => {
    if (userListWindow !== null && !userListWindow.isDestroyed()) {
      userListWindow.close();
    }
  },

  updateUsers: (users: User[]) => {
    userListWindow?.webContents.send("onUpdateUsers", users);
  },
};

ipcMain.on("requestUsers", () => {
  controller.requestUserList();
});
