import {
  CustomPointerType,
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
import { store } from "@/store";
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
  toggleOverlayWindowDevToolsInOverlayWindow,
} from "@/view/window/pointerOverlay";

export const initialize = () => {
  model.initialize(store.get("customPointerTypes"));

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
  await showOverlayWindow();
  await showInviteLink();
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

export const showInviteLink = async () => {
  await showInviteLinkWindow();
};

export const toggleOverlayWindowDevTools = () => {
  toggleOverlayWindowDevToolsInOverlayWindow();
};

export const addCustomPointerType = (customPointerType: CustomPointerType) => {
  model.addedCustomPointerType(customPointerType);

  updateTray();

  store.set("customPointerTypes", model.getState().customPointerTypes);
};

export const removeCustomPointerType = (
  customPointerType: CustomPointerType
) => {
  model.removedCustomPointerType(customPointerType);

  updateTray();

  store.set("customPointerTypes", model.getState().customPointerTypes);
};

export const updateCustomPointerType = (
  customPointerType: CustomPointerType
) => {
  model.updatedCustomPointerType(customPointerType);

  updateTray();

  store.set("customPointerTypes", model.getState().customPointerTypes);
};
