import { State } from "@/types";

import { tray } from "./tray";
import { renderInviteLinkWindow } from "./window/inviteLink";
import { renderOverlayWindow } from "./window/pointerOverlay";

export const view = (state: State) => {
  tray(state);
  renderInviteLinkWindow(state);
  renderOverlayWindow(state);
};
