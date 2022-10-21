import { BrowserWindow } from "electron";

import type { AppState } from "@/AppState";
import type { Coordinate, User } from "@/types";
import { loadWindow } from "@/window";

let overlayWindow: BrowserWindow | null = null;

export const showOverlayWindow = async (appState: AppState) => {
  if (appState.state.name !== "CREATED") {
    throw new Error("なにしとんねん");
  }

  if (overlayWindow === null) {
    overlayWindow = new BrowserWindow({
      width: 600,
      height: 400,
      title: "スマートポインター",
      show: false,
    });
  }

  loadWindow(overlayWindow, "overlay.html");

  overlayWindow.show();
};

export const movePointer =
  (appState: AppState) => (userId: User["id"], coordinate: Coordinate) => {
    // TODO: overlayWindow に教えてあげる
    if (appState.state.name !== "CREATED") {
      throw new Error('な"ん"し"と"ん"ね"ん"');
    }
    appState.setState((state) => {
      if (state.name !== "CREATED") {
        throw new Error("んいやなんしとんねん");
      }
      return {
        ...state,
        pointers: state.pointers.set(userId, coordinate),
      };
    });
  };
