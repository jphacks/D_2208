import type {
  CustomPointerType,
  GetPointerResult,
  PointerType,
  UpdatePointersMessage,
  User,
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

export const onUpdateCustomPointerTypes = (
  callback: (customPointerTypes: CustomPointerType[]) => void
) => {
  ipcRenderer.on(
    "onUpdateCustomPointerTypes",
    (_, customPointerTypes: CustomPointerType[]) => {
      callback(customPointerTypes);
    }
  );
};

export const getCustomPointerTypes = async (): Promise<CustomPointerType[]> => {
  const customPointerTypes: CustomPointerType[] = await ipcRenderer.invoke(
    "getCustomPointerTypes"
  );
  return customPointerTypes;
};

export const updateCustomPointerType = (
  customPointerType: CustomPointerType
) => {
  ipcRenderer.send("updateCustomPointerType", customPointerType);
};

export const addCustomPointerType = () => {
  ipcRenderer.send("addCustomPointerType");
};

export const removeCustomPointerType = (
  customPointerType: CustomPointerType
) => {
  ipcRenderer.send("removeCustomPointerType", customPointerType);
};

export const requestUsers = () => {
  ipcRenderer.send("requestUsers");
};

export const onUpdateUsers = (callback: (users: User[]) => void) => {
  ipcRenderer.on("onUpdateUsers", (_, users: User[]) => {
    callback(users);
  });
};
