import { app } from "electron";
import { join } from "path";

export const assetsPath = join(
  app.isPackaged ? process.resourcesPath : app.getAppPath(),
  "assets"
);
