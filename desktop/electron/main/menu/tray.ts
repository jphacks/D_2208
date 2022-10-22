import { Menu, Tray } from "electron";
import { join } from "path";

import { publicPath } from "../path";

import { renderMenuTemplate } from "./template";

export const createTray = ({
  appState,
  ...menuTemplateParam
}: Parameters<typeof renderMenuTemplate>[0]) => {
  const iconPath = join(publicPath, "assets/menu-bar-icon.png");
  const tray = new Tray(iconPath);
  tray.setToolTip("スマートポインター");

  const contextMenu = Menu.buildFromTemplate(
    renderMenuTemplate({ ...menuTemplateParam, appState })
  );
  tray.setContextMenu(contextMenu);

  appState.subscribe(() => {
    tray.setContextMenu(
      Menu.buildFromTemplate(
        renderMenuTemplate({ ...menuTemplateParam, appState })
      )
    );
  });
};
