import type {
  GetPointerResult,
  PointerType,
  UpdatePointersMessage,
} from "@smartpointer-desktop/shared";
import { ipcRenderer } from "electron";

export const onUpdatePointers = (
  callback: (message: UpdatePointersMessage) => void
) => {
  ipcRenderer.on("onUpdatePointers", (_, message: UpdatePointersMessage) => {
    callback(message);
  });
};

export const onUpdatePointerType = (
  callback: (message: PointerType) => void
) => {
  ipcRenderer.on("onUpdatePointerType", (_, message: PointerType) => {
    callback(message);
  });
};

export const getPointers = async (): Promise<GetPointerResult> => {
  const pointers: GetPointerResult = await ipcRenderer.invoke("getPointers");
  return pointers;
};
