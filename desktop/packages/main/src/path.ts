import { app } from "electron";
import { join } from "path";

export const buildResourcesPath = join(
  app.isPackaged ? process.resourcesPath : app.getAppPath(),
  "buildResources"
);
