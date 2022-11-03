import { app } from "electron";

import { initialize } from "./controller";

app.once("ready", async () => {
  initialize();
});

app.on("window-all-closed", () => {
  // prevent the app from quitting when all windows are closed
  // the app will quit when the user clicks the tray menu
});
