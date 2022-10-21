import { contextBridge } from "electron";

export type ElectronApi = {};

const electronApi: ElectronApi = {};

contextBridge.exposeInMainWorld("electronApi", electronApi);
