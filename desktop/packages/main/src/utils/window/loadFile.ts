import { app, BrowserWindow } from "electron";

const base =
  import.meta.env.DEV && import.meta.env.VITE_DEV_SERVER_URL !== undefined
    ? import.meta.env.VITE_DEV_SERVER_URL
    : `file://${app.getAppPath()}/packages/renderer/dist/`;

export const loadFile = async (
  window: BrowserWindow,
  fileName: string,
  params?: { [K in string]: string }
) => {
  const url = new URL(fileName, base);

  if (params !== undefined) {
    Object.entries(params).forEach(([key, value]) => {
      url.searchParams.append(key, value);
    });
  }

  await window.loadURL(url.toString());
};
