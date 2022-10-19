import { BrowserWindow } from "electron";

import { buildTitle } from "@/util/window";

export class InviteLinkWindow {
  private window = new BrowserWindow({
    width: 600,
    height: 400,
    title: buildTitle("招待リンク"),
  });
}
