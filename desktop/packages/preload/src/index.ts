import type { PointerCoordinate } from "@smartpointer-desktop/shared";
import { ipcRenderer } from "electron";

export const onUpdatePointerPosition = (
  callback: (position: PointerCoordinate) => void
) => {
  ipcRenderer.on(
    "update-pointer-position",
    (_, position: PointerCoordinate) => {
      callback(position);
    }
  );
};

export const onHidePointer = (callback: () => void) => {
  ipcRenderer.on("hide-pointer", () => {
    callback();
  });
};
