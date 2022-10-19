import type { MenuItemConstructorOptions } from "electron";

import type AppState from "@/AppState";

const renderDefaultMenuTemplate = (
  appState: AppState,
  handleClickCreateRoom: () => void
): MenuItemConstructorOptions[] => [
  {
    label: "ルームを作成",
    accelerator: "CmdOrCtrl+N",
    enabled: appState.state.name !== "CREATING",
    click: handleClickCreateRoom,
  },
];

const renderCreatedMenuTemplate = (
  appState: AppState
): MenuItemConstructorOptions[] => [
  {
    label: "参加者一覧",
    accelerator: "CmdOrCtrl+L",
    // TODO: チェックリストに変更する
    submenu:
      Array.from(appState.state.users?.values() ?? []).map((user) => ({
        label: user.name,
      })) ?? [],
  },
  {
    label: "招待リンクを表示",
    accelerator: "CmdOrCtrl+S",
  },
];

export const renderMenuTemplate = ({
  appState,
  handleClickCreateRoom,
}: {
  appState: AppState;
  handleClickCreateRoom: () => void;
}): MenuItemConstructorOptions[] => [
  ...(appState.state.name === "CREATED"
    ? renderCreatedMenuTemplate(appState)
    : renderDefaultMenuTemplate(appState, handleClickCreateRoom)),
  { type: "separator" },

  { role: "quit", label: "アプリを終了" },
];
