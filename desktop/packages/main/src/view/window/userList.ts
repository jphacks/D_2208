import { User } from "@smartpointer-desktop/shared";
import { app, BrowserWindow, ipcMain } from "electron";
import { join } from "node:path";

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
    userListWindow?.close();
  },
};

ipcMain.handle("getUsers", () => {
  // TODO: ここで、ユーザー一覧を取得する
  const users: User[] = [{ id: "1", name: "太郎" }];
  return users;
});
