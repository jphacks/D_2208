import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import { ipcRenderer } from "electron";

export const onUpdatePointers = (
  callback: (message: UpdatePointersMessage) => void
) => {
  ipcRenderer.on("pointers-updated", (_, message: UpdatePointersMessage) => {
    callback(message);
  });
};
