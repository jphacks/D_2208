import { Menu, MenuItemConstructorOptions, Tray } from "electron";
import { join } from "path";

import { assetsPath } from "../path";

import * as controller from "@/controller";
import { State } from "@/types";

type MenuTemplate = (state: State) => MenuItemConstructorOptions[];

const defaultMenuTemplate: MenuTemplate = (state) => [
  {
    label: "ルームを作成",
    accelerator: "CmdOrCtrl+N",
    enabled: state.status !== "CREATING",
    click: controller.createRoom,
  },
];

const createdRoomMenuTemplate: MenuTemplate = (state) => [
  {
    label: "参加者一覧",
    accelerator: "CmdOrCtrl+L",
    // TODO: チェックリストに変更する
    submenu:
      Array.from(state.joinedUsers?.values() ?? []).map((user) => ({
        label: user.name,
      })) ?? [],
  },
  {
    label: "招待リンクを表示",
    accelerator: "CmdOrCtrl+S",
    click: controller.showInviteLink,
  },
];

const menuTemplate: MenuTemplate = (state) => [
  ...(state.status === "CREATED"
    ? createdRoomMenuTemplate(state)
    : defaultMenuTemplate(state)),
  { type: "separator" },
  {
    role: "quit",
    label: "アプリを終了",
    accelerator: "Cmd+Q",
  },
  ...(import.meta.env.DEV && state.status === "CREATED"
    ? ([
        { type: "separator" },
        {
          label: "オーバーレイの開発者ツールを表示",
          click: controller.toggleOverlayWindowDevTools,
        },
      ] as const)
    : []),
];

let trayInstance: Tray | null = null;

export const tray = (state: State) => {
  if (trayInstance === null) {
    const iconPath = join(assetsPath, "menu-bar-icon.png");

    trayInstance = new Tray(iconPath);

    trayInstance.setToolTip("スマートポインター");
  }

  trayInstance.setContextMenu(Menu.buildFromTemplate(menuTemplate(state)));
};
