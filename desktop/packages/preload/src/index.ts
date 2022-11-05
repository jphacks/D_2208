import type {
  GetPointerResult,
  PointerType,
  UpdatePointersMessage,
} from "@smartpointer-desktop/shared";
import { ipcRenderer } from "electron";

export const onUpdatePointers = (
  callback: (message: UpdatePointersMessage) => void
) => {
  ipcRenderer.on("pointers-updated", (_, message: UpdatePointersMessage) => {
    callback(message);
  });
};

export const onUpdatePointerType = (
  callback: (message: PointerType) => void
) => {
  ipcRenderer.on("pointer-type-updated", (_, message: PointerType) => {
    callback(message);
  });
};

export const getPointers = async (): Promise<GetPointerResult> => {
  const pointers: GetPointerResult = await ipcRenderer.invoke("get-pointers");
  return pointers;
};
