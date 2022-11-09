import { builtInPointers } from "@smartpointer-desktop/shared";
import { Menu, MenuItemConstructorOptions, nativeTheme, Tray } from "electron";
import { join } from "path";

import { assetsPath } from "../path";

import { controller } from "@/controller";
import { model } from "@/model";
import { State } from "@/types";

type MenuTemplate<S extends State = State> = (
  state: S
) => MenuItemConstructorOptions[];

const defaultMenuTemplate: MenuTemplate<
  State & { status: "CREATING" | "READY" }
> = (state) => [
  {
    label: "ルームを作成",
    accelerator: "CmdOrCtrl+N",
    enabled: state.status !== "CREATING",
    click: controller.createRoom,
  },
];

const createdRoomMenuTemplate: MenuTemplate<State & { status: "CREATED" }> = (
  state
) => [
  {
    label: "ポインター",
    submenu: builtInPointers
      .concat(
        state.customPointerTypes.filter(
          (pointerType) => pointerType.image !== undefined
        )
      )
      .map((pointer) => ({
        label: pointer.name,
        type: "radio",
        checked: state.selectedPointerType.id === pointer.id,
        click: () => controller.selectedPointer(pointer),
      })),
  },
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
  { type: "separator" },
  {
    label: "ルームを終了",
    accelerator: "CmdOrCtrl+W",
    click: controller.closeRoom,
  },
];

const menuTemplate: MenuTemplate = (state) => [
  ...(state.status === "CREATED"
    ? createdRoomMenuTemplate(state)
    : defaultMenuTemplate(state)),
  { type: "separator" },
  {
    label: "自作ポインターの設定",
    click: controller.showCustomPointerTypes,
  },
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

const getIconFileName = () => {
  if (process.platform === "darwin") {
    return "tray-iconTemplate.png";
  }

  if (process.platform === "linux") {
    return "tray-icon-white.png";
  }

  if (process.platform === "win32") {
    if (nativeTheme.shouldUseDarkColors) {
      return "tray-icon-white.ico";
    }
    return "tray-icon.ico";
  }

  if (nativeTheme.shouldUseDarkColors) {
    return "tray-icon-white.png";
  }
  return "tray-icon.png";
};

export const tray = {
  update: () => {
    const state = model.state;

    if (trayInstance === null) {
      const iconPath = join(assetsPath, getIconFileName());

      trayInstance = new Tray(iconPath);

      trayInstance.setToolTip("スマートポインター");
    }

    trayInstance.setContextMenu(Menu.buildFromTemplate(menuTemplate(state)));
  },
};

nativeTheme.on("updated", () => {
  if (trayInstance !== null) {
    trayInstance.setImage(join(assetsPath, getIconFileName()));
  }
});
