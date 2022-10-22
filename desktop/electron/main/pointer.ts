import { BrowserWindow, screen } from "electron";

import type { AppState } from "./AppState";
import type { Coordinate } from "./types";
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
        nodeIntegration: true,
        contextIsolation: false,
      },
    });
  }
  overlayWindow.setAlwaysOnTop(true, "screen-saver");
  overlayWindow.setVisibleOnAllWorkspaces(true);
  overlayWindow.setIgnoreMouseEvents(true);

  loadWindow(overlayWindow, "overlay.html");

  overlayWindow.show();
};

export const toggleOverlayWindowDevTools = () => {
  if (overlayWindow === null) {
    return;
  }
  overlayWindow.webContents.toggleDevTools();
};

export const sendPointerPosition = (position: Coordinate) => {
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
