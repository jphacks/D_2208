import {
  PointerOrientation,
  PointerType,
  User,
} from "@smartpointer-desktop/shared";

import { roomApi } from "@/api";
import * as model from "@/model";
import {
  activate,
  listenRoomSubscription,
  unsubscribeRoomSubscription,
} from "@/stomp";
import { updateTray } from "@/view/tray";
import {
  closeInviteLinkWindow,
  showInviteLinkWindow,
} from "@/view/window/inviteLink";
import {
  closeOverlayWindow,
  showOverlayWindow,
  updatePointerInOverlayWindow,
  updatePointerTypeInOverlayWindow,
} from "@/view/window/pointerOverlay";

export const initialize = () => {
  model.initialize();

  updateTray();
};

export const createRoom = async () => {
  model.startCreatingRoom();

  const { data } = await roomApi.createRoom();

  console.log("created room", data);

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

export const closeRoom = () => {
  closeOverlayWindow();
  closeInviteLinkWindow();
  unsubscribeRoomSubscription();

  model.closeRoom();
};

export const pointerUpdated = (user: User, orientation: PointerOrientation) => {
  model.updatePointer(user, orientation);

  updatePointerInOverlayWindow();
};

export const pointerDeactivated = (user: User) => {
  model.deactivatePointer(user);

  updatePointerInOverlayWindow();
};

export const selectedPointer = (selectedPointerType: PointerType) => {
  model.selectedPointer(selectedPointerType);

  updatePointerTypeInOverlayWindow();
};

export const showInviteLink = () => {
  showInviteLinkWindow();
};

export const toggleOverlayWindowDevTools = () => {
  toggleOverlayWindowDevTools();
};
