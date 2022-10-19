import { app, BrowserWindow } from "electron";
import electronReload from "electron-reload";

import { AppState } from "@/AppState";
import { createTray } from "@/menu/tray";
import { distPath } from "@/path";
import { createRoom } from "@/room";

const createWindow = async () => {
  const mainWindow = new BrowserWindow({
    width: 600,
    height: 400,
    title: "スマートポインター",
  });

  if (!app.isPackaged) {
    await mainWindow.loadURL("http://localhost:5173");
    electronReload(__dirname, {
      electron: require(`${__dirname}/../../node_modules/electron`),
    });
    // TODO: dev tool をトグルできるようにする
    mainWindow.webContents.openDevTools();
  } else {
    mainWindow.loadURL(`file://${distPath}/index.html`);
  }
};

app.once("ready", () => {
  createWindow();
  const appState = new AppState();
  createTray({
    appState,
    handleClickCreateRoom: createRoom({ appState }),
  });
});

app.once("window-all-closed", () => app.quit());
