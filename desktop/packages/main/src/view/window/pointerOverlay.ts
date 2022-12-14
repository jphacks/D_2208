import {
  builtInPointers,
  GetPointerResult,
  PointerType,
  UpdatePointersMessage,
} from "@smartpointer-desktop/shared";
import { app, BrowserWindow, ipcMain, screen } from "electron";
import { join } from "node:path";

import { model } from "@/model";
import { loadFile } from "@/utils/window/loadFile";

let overlayWindow: BrowserWindow | null = null;

const tanDeg = (deg: number) => Math.tan((deg * Math.PI) / 180);

export const pointerOverlay = {
  show: async () => {
    const state = model.state;

    if (state.status !== "CREATED") {
      throw new Error("Cannot show overlay window when not in CREATED state");
    }

    overlayWindow = new BrowserWindow({
      title: "スマートポインター",
      show: false,
      frame: false,
      transparent: true,
      focusable: false,
      hasShadow: false,
      webPreferences: {
        // load custom pointer image from local file
        webSecurity: !import.meta.env.DEV,
        preload: join(
          app.getAppPath(),
          "packages",
          "preload",
          "dist",
          "index.cjs"
        ),
      },
    });

    overlayWindow.setAlwaysOnTop(true, "screen-saver");
    overlayWindow.setVisibleOnAllWorkspaces(true, {
      visibleOnFullScreen: true,
    });
    overlayWindow.setIgnoreMouseEvents(true);

    await loadFile(overlayWindow, "overlay.html");

    pointerOverlay.setBoundsToDisplay();

    overlayWindow.show();
  },

  toggleDevTools: () => {
    if (overlayWindow === null || overlayWindow.isDestroyed()) {
      return;
    }

    if (!overlayWindow.webContents.isDevToolsOpened()) {
      overlayWindow.webContents.openDevTools({
        mode: "detach",
      });
    } else {
      overlayWindow.webContents.closeDevTools();
    }
  },

  updatePointer: () => {
    const state = model.state;

    if (state.status !== "CREATED") {
      throw new Error("Cannot update pointer when not in CREATED state");
    }

    if (overlayWindow === null || overlayWindow.isDestroyed()) {
      return;
    }

    const message: UpdatePointersMessage = [
      ...state.activePointers.values(),
    ].map(({ user, orientation }) => ({
      user,
      coordinate: {
        x: -tanDeg(orientation.alpha) / 2,
        y: -tanDeg(orientation.beta) / 2,
      },
    }));

    overlayWindow.webContents.send("onUpdatePointers", message);
  },

  updatePointerType: () => {
    const state = model.state;

    if (state.status !== "CREATED") {
      throw new Error("Cannot update pointer type when not in CREATED state");
    }

    if (overlayWindow === null || overlayWindow.isDestroyed()) {
      return;
    }

    const message: PointerType = builtInPointers
      .concat(state.customPointerTypes)
      .find((pointerType) => pointerType.id === state.selectedPointerTypeId)!;

    overlayWindow.webContents.send("onUpdatePointerType", message);
  },

  setBoundsToDisplay: () => {
    const state = model.state;

    if (state.status !== "CREATED") {
      throw new Error("Cannot set bounds when not in CREATED state");
    }

    if (overlayWindow === null || overlayWindow.isDestroyed()) {
      return;
    }

    const display = screen
      .getAllDisplays()
      .find((display) => display.id === state.displayToShowPointer);

    if (display === undefined) {
      throw new Error("Cannot find display to show pointer");
    }

    overlayWindow.setBounds(
      {
        x: display.bounds.x,
        y: display.bounds.y,
        width: display.size.width,
        height: display.size.height,
      },
      false
    );
  },

  close: () => {
    if (overlayWindow !== null && !overlayWindow.isDestroyed()) {
      overlayWindow.close();
      overlayWindow = null;
    }
  },
};

ipcMain.handle("getPointers", (): GetPointerResult => {
  const state = model.state;

  if (state.status !== "CREATED") {
    throw new Error("Cannot get pointers when not in CREATED state");
  }

  return {
    pointers: [...state.activePointers.values()].map(
      ({ user, orientation }) => ({
        user,
        coordinate: {
          x: -tanDeg(orientation.alpha) / 2,
          y: -tanDeg(orientation.beta) / 2,
        },
      })
    ),
    pointerType: builtInPointers
      .concat(state.customPointerTypes)
      .find((pointerType) => pointerType.id === state.selectedPointerTypeId)!,
  };
});
