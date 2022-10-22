import { contextBridge, ipcRenderer } from "electron";

// import type { Coordinate } from "../main/types";

console.log("preload");
export type ElectronApi = {
  onUpdateRotation: (
    callback: (position: { x: number; y: number }) => void
  ) => void;
  hidePointer: (callback: () => void) => void;
};

const electronApi: ElectronApi = {
  onUpdateRotation: (callback: (position: { x: number; y: number }) => void) =>
    ipcRenderer.on("update-pointer-position", (_, data) => callback(data)),

  hidePointer: (callback: () => void) =>
    ipcRenderer.on("hide-pointer", callback),
};

contextBridge.exposeInMainWorld("electronApi", electronApi);

console.log("preload");
