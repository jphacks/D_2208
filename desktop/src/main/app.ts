import { app, BrowserWindow } from "electron";
import electronReload from "electron-reload";

import createMenu from "./menu";

const createWindow = async () => {
  const mainWindow = new BrowserWindow({
    width: 600,
    height: 400,
    title: "スマートポインター",
  });

  if (process.env.NODE_ENV === "DEV") {
    await mainWindow.loadURL("http://localhost:5173");
    electronReload(__dirname, {
      electron: require(`${__dirname}/../node_modules/electron`),
    });
    // TODO: dev tool をトグルできるようにする
    mainWindow.webContents.openDevTools();
  } else {
    mainWindow.loadURL(`file://${__dirname}/index.html`);
  }
};

app.once("ready", () => {
  createMenu();
  createWindow();
});

app.once("window-all-closed", () => app.quit());
