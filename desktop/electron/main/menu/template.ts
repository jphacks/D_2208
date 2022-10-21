import type { MenuItemConstructorOptions } from "electron";

import type AppState from "@/AppState";

const renderDefaultMenuTemplate = ({
  appState,
  handleClickCreateRoom,
}: {
  appState: AppState;
  handleClickCreateRoom: () => void;
}): MenuItemConstructorOptions[] => [
  {
    label: "ルームを作成",
    accelerator: "CmdOrCtrl+N",
    enabled: appState.state.name !== "CREATING",
    click: handleClickCreateRoom,
  },
];

const renderCreatedMenuTemplate = ({
  appState,
  handleClickShowInviteLink,
}: {
  appState: AppState;
  handleClickShowInviteLink: () => void;
}): MenuItemConstructorOptions[] => [
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
    click: handleClickShowInviteLink,
  },
];

export const renderMenuTemplate = ({
  appState,
  handleClickCreateRoom,
  handleClickShowInviteLink,
}: {
  appState: AppState;
  handleClickCreateRoom: () => void;
  handleClickShowInviteLink: () => void;
}): MenuItemConstructorOptions[] => [
  ...(appState.state.name === "CREATED"
    ? renderCreatedMenuTemplate({ appState, handleClickShowInviteLink })
    : renderDefaultMenuTemplate({ appState, handleClickCreateRoom })),
  { type: "separator" },

  {
    role: "quit",
    label: "アプリを終了",
    accelerator: "Cmd+Q",
  },
];
