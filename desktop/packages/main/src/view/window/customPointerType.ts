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

      customPointerTypesWindow.webContents.openDevTools();

      await loadFile(customPointerTypesWindow, "customPointerTypes.html");
    }

    customPointerTypesWindow.removeMenu();
    customPointerTypesWindow.show();
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
  "addCustomPointerType",
  (_, { label, content }: { label: string; content: string }) => {
    controller.addCustomPointerType(label, content);
  }
);

ipcMain.on(
  "removeCustomPointerType",
  (_, customPointerTypeId: CustomPointerType["id"]) => {
    controller.removeCustomPointerType(customPointerTypeId);
  }
);
