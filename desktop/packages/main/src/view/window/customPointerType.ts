import { CustomPointerType } from "@smartpointer-desktop/shared";
import { app, BrowserWindow, ipcMain } from "electron";
import { join } from "path";

import { controller } from "@/controller";
import { model } from "@/model";
import { loadFile } from "@/utils/window/loadFile";

let customPointerTypesWindow: BrowserWindow | null = null;

export const customPointerType = {
  show: async () => {
    if (
      customPointerTypesWindow === null ||
      customPointerTypesWindow.isDestroyed()
    ) {
      customPointerTypesWindow = new BrowserWindow({
        width: 800,
        height: 600,
        title: "カスタムポインターの設定 - スマートポインター",
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

      await loadFile(customPointerTypesWindow, "customPointerTypes.html");
    }

    customPointerTypesWindow.show();
    customPointerTypesWindow.removeMenu();
  },

  close: () => {
    customPointerTypesWindow?.close();
  },

  updateCustomPointerType: () => {
    const { customPointerTypes } = model.state;
    customPointerTypesWindow?.webContents.send(
      "onUpdateCustomPointerTypes",
      customPointerTypes
    );
  },
};

ipcMain.handle("getCustomPointerTypes", () => {
  return model.state.customPointerTypes;
});

ipcMain.on(
  "updateCustomPointerType",
  (_, customPointerType: CustomPointerType) => {
    controller.updateCustomPointerType(customPointerType);
  }
);

ipcMain.on("addCustomPointerType", () => {
  controller.addCustomPointerType();
});

ipcMain.on(
  "removeCustomPointerType",
  (_, customPointerType: CustomPointerType) => {
    controller.removeCustomPointerType(customPointerType);
  }
);
