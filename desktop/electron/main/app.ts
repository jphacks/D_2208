import { app, BrowserWindow } from "electron";
import electronReload from "electron-reload";

import { HttpClient } from "@/api/HttpClient";
import { StompClient } from "@/api/StompClient";
import { Pagination } from "@/feature/pagination/Pagination";
import { Pointer } from "@/feature/pointer/Pointer";
import { Room } from "@/feature/room/Room";

app.once("ready", () => {
  const httpClient = new HttpClient();
  const stompClient = new StompClient();

  const features = {
    room: new Room(httpClient, stompClient),
    pointer: new Pointer(httpClient, stompClient),
    pagination: new Pagination(httpClient, stompClient),
  };

  Promise.all(Object.values(features).map((feature) => feature.run()));
});

app.once("window-all-closed", () => app.quit());
