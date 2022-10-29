import { ipcRenderer } from "electron";

export type Coordinate = { x: number; y: number };

export const onUpdatePointerPosition = (
  callback: (position: Coordinate) => void
) => {
  ipcRenderer.on("update-pointer-position", (_, position: Coordinate) => {
    callback(position);
  });
};

export const onHidePointer = (callback: () => void) => {
  ipcRenderer.on("hide-pointer", () => {
    callback();
  });
};
