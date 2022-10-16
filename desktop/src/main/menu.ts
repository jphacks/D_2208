/** @format */

import { app, Menu, MenuItemConstructorOptions } from "electron";
import AppState from "./AppState";
import showShereLink from "./showShereLink";

const defaultMenu = (appState: AppState): MenuItemConstructorOptions[] => [
  {
    label: app.name,
    submenu: [
      {
        label: "ルームを作成",
        accelerator: "CmdOrCtrl+N",
        enabled: !appState.isActivating,
        click: appState.createRoom,
      },
      { type: "separator" },
      { role: "quit", label: "アプリを終了" },
    ],
  },
];

const roomMenu = (appState: AppState): MenuItemConstructorOptions[] => [
  {
    label: app.name,
    submenu: [
      {
        label: "参加者一覧",
        accelerator: "CmdOrCtrl+L",
        // TODO: チェックリストに変更する
        submenu: appState.room?.users.map((user) => ({ label: user })) ?? [],
      },
      {
        label: "招待リンクを表示",
        accelerator: "CmdOrCtrl+S",
        click: showShereLink,
      },
      { type: "separator" },
      {
        label: "ルームを終了",
        accelerator: "CmdOrCtrl+W",
        click: appState.deleteRoom,
      },
      { type: "separator" },
      { role: "quit", label: "アプリを終了" },
    ],
  },
];

const createMenu = () => {
  const appState = new AppState();
  const menu = Menu.buildFromTemplate(
    appState.room ? roomMenu(appState) : defaultMenu(appState)
  );
  Menu.setApplicationMenu(menu);
};

export default createMenu;
