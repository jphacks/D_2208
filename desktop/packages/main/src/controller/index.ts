import { PointerCoordinate } from "@smartpointer-desktop/shared";

import { roomApi } from "@/api";
import * as model from "@/model";
import { activate, listenRoomSubscription } from "@/stomp";
import { User } from "@/types";
import { updateTray } from "@/view/tray";
import { showInviteLinkWindow } from "@/view/window/inviteLink";
import {
  showOverlayWindow,
  updatePointerInOverlayWindow,
} from "@/view/window/pointerOverlay";

export const initialize = () => {
  model.initialize();

  updateTray();
};

export const createRoom = async () => {
  model.startCreatingRoom();

  const { data } = await roomApi.createRoom();

  await activate();

  model.createdRoom(data);

  listenRoomSubscription(data.roomId);

  updateTray();
  showInviteLink();
  showOverlayWindow();
};

export const joinedRoom = (user: User) => {
  model.joinedRoom(user);
};

export const leftRoom = (user: User) => {
  model.leftRoom(user);
};

export const pointerUpdated = (
  userId: User["id"],
  pointer: PointerCoordinate
) => {
  model.updatePointer(userId, pointer);

  updatePointerInOverlayWindow();
};

export const pointerDeactivated = (userId: User["id"]) => {
  model.deactivatePointer(userId);

  updatePointerInOverlayWindow();
};

export const showInviteLink = () => {
  showInviteLinkWindow();
};

export const toggleOverlayWindowDevTools = () => {
  toggleOverlayWindowDevTools();
};
