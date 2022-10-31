import { app, BrowserWindow, screen } from "electron";
import { join } from "node:path";

import * as controller from "@/controller";
import { State } from "@/types";

import { loadFile } from "./loadFile";

let overlayWindow: BrowserWindow | null = null;

export const renderOverlayWindow = async (state: State) => {
  if (state.status !== "CREATED") {
    if (overlayWindow && !overlayWindow.isDestroyed()) {
      overlayWindow.close();
    }
    overlayWindow = null;
    return;
  }

  const { width, height } = screen.getPrimaryDisplay().workAreaSize;

  if (overlayWindow === null || overlayWindow.isDestroyed()) {
    overlayWindow = new BrowserWindow({
      width,
      height,
      title: "スマートポインター",
      show: false,
      frame: false,
      transparent: true,
      focusable: false,
      hasShadow: false,
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

    overlayWindow.setAlwaysOnTop(true, "screen-saver");
    overlayWindow.setVisibleOnAllWorkspaces(true, {
      visibleOnFullScreen: true,
    });
    overlayWindow.setIgnoreMouseEvents(true);

    await loadFile(overlayWindow, "overlay.html");

    overlayWindow.show();
  }

  if (state.showOverlayWindowDevTools) {
    if (!overlayWindow.webContents.isDevToolsOpened()) {
      overlayWindow.webContents.openDevTools({
        mode: "detach",
      });

      overlayWindow.webContents.on(
        "devtools-closed",
        controller.closeOverlayWindowDevTools
      );
    }
  } else {
    if (overlayWindow.webContents.isDevToolsOpened()) {
      overlayWindow.webContents.closeDevTools();
    }
  }

  overlayWindow.webContents.send(
    "pointers-updated",
    [...state.activePointers.entries()].map(([userId, pointer]) => ({
      userId,
      pointer,
      name: state.joinedUsers.get(userId)?.name ?? "",
    }))
  );
};
