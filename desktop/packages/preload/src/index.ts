import type { Pointers } from "@smartpointer-desktop/shared";
import { ipcRenderer } from "electron";

export const onUpdatePointers = (callback: (pointers: Pointers) => void) => {
  ipcRenderer.on("pointers-updated", (_, pointers: Pointers) => {
    callback(pointers);
  });
};
