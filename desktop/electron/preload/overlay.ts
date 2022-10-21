import { contextBridge } from "electron";

// eslint-disable-next-line @typescript-eslint/ban-types
export type ElectronApi = {};

const electronApi: ElectronApi = {};

contextBridge.exposeInMainWorld("electronApi", electronApi);
