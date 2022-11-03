import { app, BrowserWindow, screen } from "electron";
import { join } from "node:path";

import { getState } from "@/model";

import { loadFile } from "./loadFile";

let overlayWindow: BrowserWindow | null = null;

export const showOverlayWindow = async () => {
  const state = getState();

  if (state.status !== "CREATED") {
    throw new Error("Cannot show overlay window when not in CREATED state");
  }

  const { width, height } = screen.getPrimaryDisplay().workAreaSize;
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
};

export const toggleOverlayWindowDevTools = () => {
  if (overlayWindow === null || overlayWindow.isDestroyed()) {
    return;
  }

  if (overlayWindow.webContents.isDevToolsOpened()) {
    overlayWindow.webContents.openDevTools({
      mode: "detach",
    });
  } else {
    overlayWindow.webContents.closeDevTools();
  }
};

export const updatePointerInOverlayWindow = () => {
  const state = getState();

  if (state.status !== "CREATED") {
    throw new Error("Cannot update pointer when not in CREATED state");
  }

  if (overlayWindow === null || overlayWindow.isDestroyed()) {
    return;
  }

  overlayWindow.webContents.send(
    "pointers-updated",
    [...state.activePointers.entries()].map(([userId, pointer]) => ({
      userId,
      pointer,
      name: "test",
    }))
  );
};
