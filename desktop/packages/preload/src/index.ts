import type { UpdatePointersMessage } from "@smartpointer-desktop/shared";
import { ipcRenderer } from "electron";

export const onUpdatePointers = (
  callback: (message: UpdatePointersMessage) => void
) => {
  ipcRenderer.on("pointers-updated", (_, message: UpdatePointersMessage) => {
    callback(message);
  });
};

export const getPointers = async (): Promise<UpdatePointersMessage> => {
  const pointers: UpdatePointersMessage = await ipcRenderer.invoke(
    "get-pointers"
  );
  return pointers;
};
