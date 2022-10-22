import { app, BrowserWindow } from "electron";

import { distPath } from "./path";

const getURL = (fileName: string, params?: { [K in string]: string }) => {
  const url = new URL(
    fileName,
    app.isPackaged ? `file://${distPath}` : "http://localhost:7777"
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
};
