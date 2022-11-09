import { CustomPointerType } from "@smartpointer-desktop/shared";
import { app, BrowserWindow, ipcMain } from "electron";
import { join } from "path";

import * as controller from "@/controller";
import { model } from "@/model";

import { loadFile } from "./loadFile";

let customPointerTypesWindow: BrowserWindow | null = null;

export const showCustomPointerTypesWindow = async () => {
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
};

export const closeInviteLinkWindow = () => {
  customPointerTypesWindow?.close();
};

export const updateCustomPointerTypeInCustomPointerTypesWindow = () => {
  const { customPointerTypes } = model.state;
  customPointerTypesWindow?.webContents.send(
    "onUpdateCustomPointerTypes",
    customPointerTypes
  );
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
