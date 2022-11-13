import { app } from "electron";

import { controller } from "@/controller";
import { model } from "@/model";

import "./security-restrictions";

app.once("ready", async () => {
  controller.initialize();
});

app.on("window-all-closed", () => {
  // prevent the app from quitting when all windows are closed
  // the app will quit when the user clicks the tray menu
});

app.on("before-quit", () => {
  if (model.state.status === "CREATED") {
    controller.closeRoom();
  }
});
