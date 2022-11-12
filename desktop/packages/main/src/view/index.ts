import { tray } from "./tray";
import { customPointerType } from "./window/customPointerType";
import { inviteLink } from "./window/inviteLink";
import { pointerOverlay } from "./window/pointerOverlay";
import { userList } from "./window/userList";

export const view = {
  window: {
    customPointerType,
    inviteLink,
    pointerOverlay,
    userList,
  },
  tray,
};
