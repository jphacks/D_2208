import { app } from "electron";

import { controller } from "./controller";

app.once("ready", async () => {
  controller.initialize();
});

app.on("window-all-closed", () => {
  // prevent the app from quitting when all windows are closed
  // the app will quit when the user clicks the tray menu
});
