import { app, BrowserWindow, screen } from "electron";
import { join } from "node:path";

import type { AppState } from "./AppState";
import type { PointerCoordinate } from "./types";
import { loadWindow } from "./window";

let overlayWindow: BrowserWindow | null = null;

export const showOverlayWindow = async (appState: AppState) => {
  if (appState.state.name !== "CREATED") {
    throw new Error("なにしとんねん");
  }

  const { width, height } = screen.getPrimaryDisplay().workAreaSize;

  if (overlayWindow === null) {
    overlayWindow = new BrowserWindow({
      width,
      height,
      title: "スマートポインター",
      show: false,
      frame: false,
      transparent: true,
      focusable: false,
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
  }
  overlayWindow.setAlwaysOnTop(true, "screen-saver");
  overlayWindow.setVisibleOnAllWorkspaces(true, {
    visibleOnFullScreen: true,
  });
  overlayWindow.setIgnoreMouseEvents(true);

  loadWindow(overlayWindow, "overlay.html");

  overlayWindow.show();
};

export const toggleOverlayWindowDevTools = () => {
  if (overlayWindow === null) {
    return;
  }
  if (overlayWindow.webContents.isDevToolsOpened()) {
    overlayWindow.webContents.closeDevTools();
  } else {
    overlayWindow.webContents.openDevTools({
      mode: "detach",
    });
  }
};

export const sendPointerPosition = (position: PointerCoordinate) => {
  if (overlayWindow === null) {
    return;
  }
  overlayWindow.webContents.send("update-pointer-position", position);
};

export const sendHidePointer = () => {
  if (overlayWindow === null) {
    return;
  }
  overlayWindow.webContents.send("hide-pointer");
};
