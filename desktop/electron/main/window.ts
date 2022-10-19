import { app, BrowserWindow } from "electron";
import electronReload from "electron-reload";

import { distPath } from "@/path";

const getURL = (fileName: string, params?: { [K in string]: string }) => {
  const url = new URL(
    fileName,
    app.isPackaged ? `file://${distPath}` : "http://localhost:5173"
  );

  if (params !== undefined) {
    Object.entries(params).forEach(([key, value]) => {
      url.searchParams.append(key, value);
    });
  }

  return url.toString();
};

export const loadWindow = async (
  window: BrowserWindow,
  fileName: string,
  params?: { [K in string]: string }
) => {
  await window.loadURL(getURL(fileName, params));

  if (!app.isPackaged) {
    electronReload(__dirname, {
      electron: require(`${__dirname}/../../node_modules/electron`),
    });
    // TODO: dev tool をトグルできるようにする
    window.webContents.openDevTools();
  }
};
