/** @format */

import { app, Menu, MenuItemConstructorOptions } from "electron";
import createRoom from "./createRoom";
import quitRoom from "./createRoom";
import showShereLink from "./createRoom";

const defaultMenu = (isActivating: boolean): MenuItemConstructorOptions[] => [
  {
    label: app.name,
    submenu: [
      {
        label: "ルームを作成",
        accelerator: "CmdOrCtrl+N",
        enabled: !isActivating,
        click: createRoom,
      },
      { type: "separator" },
      { role: "quit", label: "アプリを終了" },
    ],
  },
];

const roomMenu = (users: string[]): MenuItemConstructorOptions[] => [
  {
    label: app.name,
    submenu: [
      {
        label: "参加者一覧",
        accelerator: "CmdOrCtrl+L",
        // TODO: チェックリストに変更する
        submenu: users.map((user) => ({ label: user })),
      },
      {
        label: "招待リンクを表示",
        accelerator: "CmdOrCtrl+S",
        click: showShereLink,
      },
      { type: "separator" },
      { label: "ルームを終了", accelerator: "CmdOrCtrl+W", click: quitRoom },
      { type: "separator" },
      { role: "quit", label: "アプリを終了" },
    ],
  },
];

const createMenu = () => {
  // TODO: これらの値はどこか別のとこで管理する
  const isRoomCreated = true;
  const isRoomActivating = true;
  const users = ["ユーザ1", "ユーザ2"];

  const menu = Menu.buildFromTemplate(
    isRoomCreated ? roomMenu(users) : defaultMenu(isRoomActivating)
  );
  Menu.setApplicationMenu(menu);
};

export default createMenu;
