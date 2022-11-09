import { tray } from "./tray";
import { customPointerType } from "./window/customPointerType";
import { inviteLink } from "./window/inviteLink";
import { pointerOverlay } from "./window/pointerOverlay";

export const view = {
  window: {
    customPointerType,
    inviteLink,
    pointerOverlay,
  },
  tray,
};
