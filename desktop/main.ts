/** @format */

import { app, BrowserWindow } from "electron";
import electronReload from "electron-reload";
import createMenu from "./src/main/menu";

const createWindow = async () => {
  const mainWindow = new BrowserWindow({
    width: 600,
    height: 400,
    title: "スマートポインター",
  });
  const isDevelopment = (process.env.NODE_ENV ?? "").trim() === "DEV";
  if (isDevelopment) {
    // Load the url of the dev server if in development mode
    // XXX: このポートは変更されるかもしれない
    await mainWindow.loadURL("http://localhost:5173");
    electronReload(__dirname, {
      electron: require(`${__dirname}/../node_modules/electron`),
    });
    if (!process.env.IS_TEST) {
      mainWindow.webContents.openDevTools();
    }
  } else {
    // Load the index.html when not in development
    mainWindow.loadURL("file://" + __dirname + "/index.html");
  }
};

app.once("ready", () => {
  createMenu();
  createWindow();
});

app.once("window-all-closed", () => app.quit());
