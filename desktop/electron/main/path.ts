import { app } from "electron";
import { join } from "path";

export const distElectronPath = join(__dirname, "..");
export const distPath = join(distElectronPath, "../dist");
export const publicPath = app.isPackaged
  ? distPath
  : join(distElectronPath, "../public");
