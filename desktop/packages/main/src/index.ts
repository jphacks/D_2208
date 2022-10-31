import { app } from "electron";

import { initialize } from "./controller";
import { activate } from "./stomp";

app.once("ready", async () => {
  activate();
  initialize();
});
