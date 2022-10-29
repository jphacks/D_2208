import { app, BrowserWindow } from "electron";

const base =
  import.meta.env.DEV && import.meta.env.VITE_DEV_SERVER_URL !== undefined
    ? import.meta.env.VITE_DEV_SERVER_URL
    : `file://${app.getAppPath()}/packages/renderer/dist/`;

const getURL = (fileName: string, params?: { [K in string]: string }) => {
  const url = new URL(fileName, base);

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
